package kr.hhplus.be.server.presentation.concert.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertSeatResponse {
    private Long id;
    private Long seatNo;
    private String status;
    private Long price;
}
