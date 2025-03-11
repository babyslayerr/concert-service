package kr.hhplus.be.server.infrastructure.reservation.kafka;

import kr.hhplus.be.server.domain.reservation.ReservationDto;
import kr.hhplus.be.server.domain.reservation.ReservationOutbox;
import kr.hhplus.be.server.domain.reservation.ReservationOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationKafkaConsumer {

    private final ReservationOutboxRepository reservationOutboxRepository;

    @KafkaListener(topics = "completedReservation",groupId = "reservation")
    public void completedReservationListener(ReservationDto reservationDto){
        // 메세지 큐의 Publishing 을 확인
        ReservationOutbox reservationOutbox = reservationOutboxRepository.findByReservationIdAndEventName(Long.valueOf(reservationDto.getId()),"CompletedReservationEvent").orElseThrow();
        reservationOutbox.published();
        reservationOutboxRepository.save(reservationOutbox);
    }

    @KafkaListener(topics = "completedPayment",groupId = "reservation")
    public void completedPaymentListener(ReservationDto reservationDto){
        // 메세지 큐의 Publishing 을 확인
        ReservationOutbox reservationOutbox = reservationOutboxRepository.findByReservationIdAndEventName(Long.valueOf(reservationDto.getId()),"CompletedPaymentEvent").orElseThrow();
        reservationOutbox.published();
        reservationOutboxRepository.save(reservationOutbox);
    }
}
