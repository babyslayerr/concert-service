package kr.hhplus.be.server.application.reservation.event;


import kr.hhplus.be.server.domain.reservation.OutboxStatus;
import kr.hhplus.be.server.domain.reservation.PaymentCreatedOutbox;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;



public record CompletedPaymentEvent(Long reservationId, Long userId, Long seatId, Long price) {
    public static PaymentCreatedOutbox toOutboxEntity(CompletedPaymentEvent event) {
        return PaymentCreatedOutbox.builder()
                .reservationId(event.reservationId)
                .status(OutboxStatus.PENDING)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
