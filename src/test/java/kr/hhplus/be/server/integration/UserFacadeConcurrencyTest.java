package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

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
    @Autowired
    private UserService userService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

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
// TODO
//    @KafkaListener(topics = "test-topic")
//    void kafkaListener(ConsumerRecord<String, String> record){
//        log.info("Listener : Key: {}, Value: {}, Partition: {}, Offset: {}",
//                record.key(), record.value(), record.partition(), record.offset());
//        userService.makePayment(1L, Long.valueOf(record.value()));
//
//    }
//
//
//    @Test
//    @DisplayName("메세지큐 topic 의 key 를 이용해 userService 분산락(순차처리 보장)을 구현할 수 있다.")
//    void 카프카_키를이용한_분산락으로_금액차감의_성공횟수만큼_잔액이차감된다() throws InterruptedException, ExecutionException {
//
//        // given
//        long originBalance = 250000L;
//        int price = 5000;
//        int successCount = 0;
//        int threadCount = 50;
//        BlockingQueue<Future<Boolean>> results = new LinkedBlockingQueue<>();
//
//        User user = userRepository.save(User.builder()
//                .id(1L)
//                .username("정종환")
//                .balance(originBalance)
//                .build());
//        userRepository.flush();
//
//
//        // when
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        for(int i = 0; i<threadCount; i++){
//            results.add(
//            executorService.submit(()->{
//                    try {
//                        kafkaTemplate.send("test-topic","userId:1",String.valueOf(price));
//                        return true;
//                    }catch (Exception e){
//                        return false;
//                    }finally {
//                        latch.countDown();
//                    }
//
//                    }
//            ));
//        }
//
//        for(int i = 0; i<threadCount;i++){
//            Future<Boolean> poll = results.poll(10, TimeUnit.SECONDS);
//            if(poll.get()){
//                successCount++;
//            }
//        }
//
//
//        executorService.shutdown();
//        // 성공 카운팅에 따른 금액이 정확하게 들어갔는지 확인
//        log.info("successCount is {}",successCount);
//
//        int totalAmount = successCount*price;
//        Assert.assertEquals(threadCount,successCount);
//        Assertions.assertEquals(originBalance-totalAmount,user.getBalance());
//    }
}

