package com.momo.server.config.auth;

import com.momo.server.dto.auth.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {//파라미터 판단
        
        //어노테이션이 checksessionuser인지 확인
        boolean isSessionUserAnnotation = methodParameter.getParameterAnnotation(CheckSessionUser.class) != null;
        //클래스가 Session유저인지 확인
        boolean isSessionUserClass = SessionUser.class.equals(methodParameter.getParameterType());
        return isSessionUserAnnotation && isSessionUserClass;
    }

    @Override//파라미터에 전달한 객체 생성
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
//세션에서 값 가져오는 기능임.
        return httpSession.getAttribute("user");
    }
}