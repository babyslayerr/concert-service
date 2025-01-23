package kr.hhplus.be.server.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    String key(); // key: 락의 이름 ex: "'userId:' + #userId"

    TimeUnit timeUnit() default TimeUnit.SECONDS; // timeUnit: 시간 단위(MILLISECONDS, SECONDS, MINUTE..)

    long waitTime() default 5L; // waitTime: 락을 획득하기 위한 대기 시간

    long leaseTime() default 3L; // leaseTime: 락을 임대하는 시간
}
