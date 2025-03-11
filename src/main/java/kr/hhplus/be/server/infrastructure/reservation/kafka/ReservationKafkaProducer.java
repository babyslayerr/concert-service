package kr.hhplus.be.server.infrastructure.reservation.kafka;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.StringValue;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationKafkaProducer {

    private final KafkaTemplate<String,ReservationDto> kafkaTemplate;

    // 브로커 예약완료 publish
    public void publishCompletedReservation(Reservation reservation) {
        kafkaTemplate.send("completedReservation",new ReservationDto(reservation));
    }

    // 브로커 결제 완료 토픽에 publish
    public void publishCompletedPayment(Reservation reservation) {
        kafkaTemplate.send("completedPayment", new ReservationDto(reservation));
    }
}
