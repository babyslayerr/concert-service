package kr.hhplus.be.server.application.reservation.dto;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.application.concert.dto.ConcertSeatResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReservationResponse {

    private Long id;
    private ConcertSeatResponse concertSeat;
    private Long price;
    private String status;
    private LocalDateTime expireAt;

    public static ReservationResponse fromEntity(Reservation reservation) {
        return ReservationResponse.builder()
                .concertSeat(ConcertSeatResponse.fromEntity(reservation.getConcertSeat()))
                .id(reservation.getId())
                .expireAt(reservation.getExpireAt())
                .status(reservation.getStatus())
                .price(reservation.getPrice())
                .build();
    }

}
