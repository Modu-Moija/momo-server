package com.momo.server.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class TimeCheckAop {

    @Around("within(com.momo.server.controller.*)")
    public Object executeTimeCheck(ProceedingJoinPoint joinPoint) throws Throwable {
	long start = System.currentTimeMillis();

	log.info("=================컨트롤러 시간측정 시작====================");
	log.info("컨트롤러 실행 메소드 : " + joinPoint.toString());

	try {
	    return joinPoint.proceed();
	} finally {
	    long finish = System.currentTimeMillis();
	    long timMs = finish - start;

	    log.info("=================컨트롤러 시간측정 종료====================");
	    log.info("컨트롤러 시간측정 종료 : " + timMs + "ms  " + joinPoint.toString());
	}
    }

}
