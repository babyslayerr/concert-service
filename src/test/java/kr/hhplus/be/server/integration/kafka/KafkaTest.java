package kr.hhplus.be.server.integration.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class KafkaTest {

    private static Logger log = LoggerFactory.getLogger(KafkaTest.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    // 비동기로 처리하기 때문에 BlockingQueue 사용
    private final BlockingQueue<String> records = new LinkedBlockingQueue<>();

    // 컨슈머 리스너 (테스트용)
    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void kafKaListener(String message) {
        log.info("test message : {}", message);
        records.add(message);
    }

    @Test
    @DisplayName("카프카 연동테스트 : 토픽에 메세지를 비동기로 수신한다.")
    void kafkaIntegrationTest() throws InterruptedException {

        // given
        String topic = "test-topic";
        String message = "Hello Kafka";

        // when
        // 메시지 전송
        kafkaTemplate.send(topic,message);


        // 메시지 수신 기다림 (최대 5초)
        String receivedMessage = records.poll(5, TimeUnit.SECONDS);

        // then
        // 컨슘한 메세지랑 프로듀스한 메세지랑 비교
        Assertions.assertEquals(message,receivedMessage);
    }


}
