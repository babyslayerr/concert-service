package kr.hhplus.be.server.presentation.reservation.dto;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.presentation.concert.dto.ConcertSeatResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReservationResponse {

    private ConcertSeatResponse concertSeat;
    private Long price;
    private String status;
    private LocalDateTime expireAt;
}
