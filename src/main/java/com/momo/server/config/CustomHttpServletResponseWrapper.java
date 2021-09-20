package com.momo.server.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if ("JSESSIONID".equals(cookie.getName())) {
            super.addHeader("Set-Cookie", getCookieValue(cookie));
        } else {
            super.addCookie(cookie);
        }
    }

    private String getCookieValue(Cookie cookie) {

        StringBuilder builder = new StringBuilder();
        builder.append(cookie.getName()).append('=').append(cookie.getValue());
        builder.append(";Path=").append(cookie.getPath());
        if (cookie.isHttpOnly()) {
            builder.append(";HttpOnly");
        }
        if (cookie.getSecure()) {
            builder.append(";Secure");
        }
        // here you can append other attributes like domain / max-age etc.
        builder.append(";SameSite=None");
        return builder.toString();
    }
}