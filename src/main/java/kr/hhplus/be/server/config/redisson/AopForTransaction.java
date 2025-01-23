package kr.hhplus.be.server.config.redisson;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopForTransaction {

    // 기존에 트랜잭션이 있든 없든 무조건 새 트랜잭션을 생성해서 실행
    // 새로운 트랜잭션을 강제로 시작해 락획득-트랜잭션시작-트랜잭션종료-락해제를 보장해주기 위함
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
