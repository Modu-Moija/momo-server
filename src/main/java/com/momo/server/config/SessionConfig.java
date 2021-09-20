package com.momo.server.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
@EnableSpringHttpSession
public class SessionConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext
            .addFilter("sessionRepositoryFilter", DelegatingFilterProxy.class)
            .addMappingForUrlPatterns(null, false, "/*");
    }

    @Bean
    public MapSessionRepository sessionRepository() {
        final Map<String, Session> sessions = new ConcurrentHashMap<>();
        MapSessionRepository sessionRepository =
            new MapSessionRepository(sessions) {
                @Override
                public void save(MapSession session) {
                    sessions.entrySet().stream()
                        .filter(entry -> entry.getValue().isExpired())
                        .forEach(entry -> sessions.remove(entry.getKey()));
                    super.save(session);
                }
            };
        sessionRepository.setDefaultMaxInactiveInterval(60*5);
        return sessionRepository;
    }

    @Bean
    public SessionRepositoryFilter<?> sessionRepositoryFilter(MapSessionRepository sessionRepository) {
        SessionRepositoryFilter<?> sessionRepositoryFilter =
            new SessionRepositoryFilter<>(sessionRepository);

        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("JSESSIONID");
        cookieSerializer.setSameSite("None");
        cookieSerializer.setUseSecureCookie(true);

        CookieHttpSessionIdResolver cookieHttpSessionIdResolver =
            new CookieHttpSessionIdResolver();
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);

        sessionRepositoryFilter.setHttpSessionIdResolver(cookieHttpSessionIdResolver);

        return sessionRepositoryFilter;
    }
}