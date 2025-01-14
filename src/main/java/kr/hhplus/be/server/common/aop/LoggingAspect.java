package kr.hhplus.be.server.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut: service 패키지의 모든 클래스의 모든 메서드
    @Pointcut("execution(* kr.hhplus.be.server.domain..*Service.*(..))")
    public void serviceMethods() {}

    // 메서드 시작 전에 로그 기록
    @Before("serviceMethods()")
    public void logMethodStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method {} Start!!!", methodName);
    }

    // 메서드 종료 후에 로그 기록
    @After("serviceMethods()")
    public void logMethodEnd(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method {} End!!!", methodName);
    }
}
