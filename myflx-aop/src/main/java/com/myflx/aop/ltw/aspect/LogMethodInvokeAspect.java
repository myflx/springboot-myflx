package com.myflx.aop.ltw.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
//@Component
public class LogMethodInvokeAspect {
    @Pointcut("execution (* com.myflx.aop.ltw.*.*(..))")
    public void pointCut() {

    }


    @Around("pointCut()")
    public void advise(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        System.out.println(signature + " start..... ");
        pjp.proceed();
        System.out.println(signature + " end......");
    }
}
