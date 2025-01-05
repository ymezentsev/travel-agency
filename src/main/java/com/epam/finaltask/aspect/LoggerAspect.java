package com.epam.finaltask.aspect;

import com.epam.finaltask.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

    @Before("@annotation(Loggable)")
    public void logMethodCall(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        if (isHideMethodArgs(joinPoint)) {
            args = new String[]{"***"};
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            log.info("Method {}.{}() invoked by '{}' with arguments: {}",
                    className, methodName, user.getUsername(), args);
        } else {
            log.info("Method {}.{}() invoked with arguments: {}", className, methodName, args);
        }
    }

    @AfterReturning(pointcut = "@annotation(Loggable)", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {}.{}() returned result: {}", className, methodName, result);
    }

    @AfterThrowing(pointcut = "@annotation(Loggable)", throwing = "e")
    public void logMethodThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() '{}'", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getMessage());
    }

    private boolean isHideMethodArgs(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(Loggable.class).hideArgs();
    }
}
