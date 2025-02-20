package kr.hhplus.be.server.infrastructure.reservation.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.StringValue;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationKafkaProducer {
    // reservationId 가 필요
    private final KafkaTemplate kafkaTemplate;

    // 브로커 예약완료 publish
    public void publishCompletedReservation(Long reservationId) {
        kafkaTemplate.send("completedReservation", String.valueOf(reservationId));
    }

    // 브로커 결제 완료 토픽에 publish
    public void publishCompletedPayment(Long reservationId) {
        kafkaTemplate.send("completedPayment", String.valueOf(reservationId));
    }
}
