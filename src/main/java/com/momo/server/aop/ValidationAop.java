package com.momo.server.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.momo.server.exception.valid.InvalidMeetException;

@Component
@Aspect
public class ValidationAop {

    @Around("execution(* com.momo.server.controller.*Controller.*(..))")
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
	// System.out.println("컨트롤러 aop 실행");

	Object[] args = proceedingJoinPoint.getArgs();
	for (Object arg : args) {
	    if (arg instanceof BindingResult) {
		BindingResult bindingResult = (BindingResult) arg;
		if (bindingResult.hasErrors()) {
		    Map<String, String> errorMap = new HashMap<>();

		    for (FieldError error : bindingResult.getFieldErrors()) {
			errorMap.put(error.getField(), error.getDefaultMessage());
		    }
		    throw new InvalidMeetException("유효성 검사 실패", errorMap);
		}

	    }
	}
	return proceedingJoinPoint.proceed();
    }

}
