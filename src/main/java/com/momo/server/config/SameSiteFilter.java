package com.momo.server.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SameSiteFilter implements Filter {

    private static final String THIRD_PARTY_URI = "/third/party/uri";


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if(THIRD_PARTY_URI.equals(request.getRequestURI())) {
            chain.doFilter(request, new CustomHttpServletResponseWrapper(response));
        } else {
            chain.doFilter(request, response);
        }
    }

}