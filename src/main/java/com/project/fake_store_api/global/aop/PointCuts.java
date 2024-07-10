package com.project.fake_store_api.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {
    @Pointcut("execution(* com.project.fake_store_api.domain..*Service.*(..))")
    public void allService() {
    }
}
