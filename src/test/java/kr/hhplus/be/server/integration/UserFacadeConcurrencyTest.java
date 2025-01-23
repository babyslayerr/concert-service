package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class UserFacadeConcurrencyTest {


    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(UserFacadeConcurrencyTest.class);

    @Test
    void 여러번_충전하면_성공한횟수만큼_금액이_저장된다_동시성테스트() throws InterruptedException, ExecutionException {
        // given
        List<Future<Boolean>> results = new ArrayList<>();

        User user = userRepository.save(User.builder()
                .username("testUser")
                .balance(0L)
                .build());

        long amount = 50000L;

        long startTime = System.currentTimeMillis();
        // thread 수
        int threadCount = 10;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for(int i=0;i<threadCount;i++){
            results.add(executorService.submit(()-> {
                        try {
                            userFacade.chargeAmount(user.getId(), amount);
                            return true;
                        } catch (Exception e) {
                            return false;
                        } finally {
                            latch.countDown();
                        }
                    }
            ));
        }

        // 서브스레드 완료전까지 메인스레드 정지
        latch.await();
        // 스레드 종료
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        log.info("service take time: {}",endTime-startTime);

        // then
        // 성공 카운팅
        int successCount = 0;
        for(Future<Boolean>  result:results){
            if(result.get()){
                successCount++;
            }
        }

        // 저장된 유저 조회
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        // 성공 카운팅에 따른 금액이 정확하게 들어갔는지 확인
        long totalAmount = successCount*amount;
        log.info("successCount is {}",successCount);
        Assertions.assertEquals(totalAmount,savedUser.getBalance());
    }
}

