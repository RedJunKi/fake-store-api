package com.project.fake_store_api.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private Long startTimeMs;
    private Long endTimeMs;

    @Around("com.project.fake_store_api.global.aop.PointCuts.allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] = {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] = {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] = {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[리소스 릴리즈] = {}", joinPoint.getSignature());
        }
    }

    @Before("com.project.fake_store_api.global.aop.PointCuts.allService()")
    public void doTimeTraceBefore(JoinPoint joinPoint) {
        startTimeMs = System.currentTimeMillis();
    }

    @After("com.project.fake_store_api.global.aop.PointCuts.allService()")
    public void doTimeTraceAfter(JoinPoint joinPoint) {
        endTimeMs = System.currentTimeMillis();
        double elapsedTimeMs = endTimeMs - startTimeMs;
        log.info("[경과시간] = {}초", elapsedTimeMs / 1000);
    }
}
