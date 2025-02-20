package kr.hhplus.be.server.application.reservation.event;

import kr.hhplus.be.server.domain.reservation.ReservationCreatedOutbox;
import kr.hhplus.be.server.domain.reservation.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;



public record CompletedReservationEvent(Long reservationId, Long userId, Long seatId, Long price) {
    public static ReservationCreatedOutbox toOutboxEntity(CompletedReservationEvent event) {
        return ReservationCreatedOutbox.builder()
                .reservationId(event.reservationId())
                .status(OutboxStatus.PENDING)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
