package kr.hhplus.be.server.domain.reservation;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// kafka 용 DTO
@Getter
@NoArgsConstructor
public class ReservationDto {
        private Long id;
        private Long userId;
        private Long concertSeatId;
        private Long price;
        private String status;
        private LocalDateTime statusUpdateAt;
        private LocalDateTime expireAt;

        public ReservationDto(Reservation reservation) {
            this.id = reservation.getId();
            this.userId = reservation.getUser().getId(); // Hibernate 프록시 문제 해결
            this.concertSeatId = reservation.getConcertSeat().getId(); // Hibernate 프록시 문제 해결
            this.price = reservation.getPrice();
            this.status = reservation.getStatus();
            this.statusUpdateAt = reservation.getStatusUpdateAt();
            this.expireAt = reservation.getExpireAt();
        }
}
