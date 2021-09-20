package com.momo.server.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig
{
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean()
    {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CookieAttributeFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}