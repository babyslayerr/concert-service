package kr.hhplus.be.server.infrastructure.reservation.kafka;

import kr.hhplus.be.server.domain.reservation.*;
import kr.hhplus.be.server.infrastructure.external.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationKafkaConsumer {

    private final ExternalApiClient apiClient;

    private final ReservationCreatedOutboxRepository reservationCreatedOutboxRepository;
    private final PaymentCreatedOutboxRepository paymentCreatedOutboxRepository;

    @KafkaListener(topics = "completedReservation",groupId = "reservation")
    public void completedReservationListener(String reservationId){
        // 메세지 큐의 Publishing 을 확인
        ReservationCreatedOutbox reservationCreatedOutbox = reservationCreatedOutboxRepository.findByReservationId(Long.valueOf(reservationId)).orElseThrow();
        reservationCreatedOutbox.published();
        reservationCreatedOutboxRepository.save(reservationCreatedOutbox);
    }

    @KafkaListener(topics = "completedPayment",groupId = "reservation")
    public void completedPaymentListener(String reservationId){
        // 메세지 큐의 Publishing 을 확인
        PaymentCreatedOutbox paymentCreatedOutbox = paymentCreatedOutboxRepository.findByReservationId(Long.valueOf(reservationId)).orElseThrow();
        paymentCreatedOutbox.published();
        paymentCreatedOutboxRepository.save(paymentCreatedOutbox);
    }
}
