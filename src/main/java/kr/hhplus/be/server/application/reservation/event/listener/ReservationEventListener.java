package kr.hhplus.be.server.application.reservation.event.listener;

import kr.hhplus.be.server.application.reservation.event.CompletedPaymentEvent;
import kr.hhplus.be.server.application.reservation.event.CompletedReservationEvent;
import kr.hhplus.be.server.domain.reservation.PaymentCreatedOutbox;
import kr.hhplus.be.server.domain.reservation.PaymentCreatedOutboxRepository;
import kr.hhplus.be.server.domain.reservation.ReservationCreatedOutbox;
import kr.hhplus.be.server.domain.reservation.ReservationCreatedOutboxRepository;
import kr.hhplus.be.server.infrastructure.reservation.kafka.ReservationKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final ReservationKafkaProducer reservationKafkaProducer;
    private final ReservationCreatedOutboxRepository reservationCreatedOutboxRepository;
    private final PaymentCreatedOutboxRepository paymentCreatedOutboxRepository;

    // 예약 완료 구독
    // 커밋전 outbox 테이블에 예약상태 저장
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveReservationCompletionOutbox(CompletedReservationEvent event){
        ReservationCreatedOutbox outbox = CompletedReservationEvent.toOutboxEntity(event);
        reservationCreatedOutboxRepository.save(outbox);
    }
    // 예약완료 구독
    // 커밋 후 브로커 서버에 publish
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReservationInfo(CompletedReservationEvent event){
        reservationKafkaProducer.publishCompletedReservation(event.reservationId());
    }

    // 결제 완료 구독
    // 커밋전 outbox 테이블에 예약상태 저장
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void savePaymentCompletionOutbox(CompletedPaymentEvent event){
        PaymentCreatedOutbox outbox = CompletedPaymentEvent.toOutboxEntity(event);
        paymentCreatedOutboxRepository.save(outbox);
    }
    // 결제완료 구독
    // 커밋 후 브로커 서버에 publish
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentInfo(CompletedPaymentEvent event){
        reservationKafkaProducer.publishCompletedPayment(event.reservationId());
    }


}
