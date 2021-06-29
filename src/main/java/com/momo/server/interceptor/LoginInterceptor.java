package com.momo.server.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
            throws Exception {

        if(response.getStatus() == 201){
            request.getSession().setAttribute("user", mav.getModel().get("authuser"));
        }

    }
}
