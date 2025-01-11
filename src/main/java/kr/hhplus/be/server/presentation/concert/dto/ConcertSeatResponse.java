package kr.hhplus.be.server.presentation.concert.dto;

import kr.hhplus.be.server.domain.concert.ConcertSeat;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConcertSeatResponse {
    private Long id;
    private Long seatNo;
    private String status;
    private Long price;

    public static ConcertSeatResponse fromEntity(ConcertSeat concertSeat) {
        return ConcertSeatResponse
                .builder()
                .id(concertSeat.getId())
                .price(concertSeat.getPrice())
                .seatNo(concertSeat.getSeatNo())
                .status(concertSeat.getStatus())
                .build();
    }
}
