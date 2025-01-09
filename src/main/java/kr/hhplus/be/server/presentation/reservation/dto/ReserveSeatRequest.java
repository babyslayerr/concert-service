package kr.hhplus.be.server.presentation.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

@Schema(description = "좌석 예약 요청 데이터")
public record ReserveSeatRequest(
        @Schema(description = "공연 날짜 (예: 2024-01-01)", example = "2024-01-01", required = true)
        String concertDate,

        @Schema(description = "좌석 번호", example = "12", required = true)
        String seatNumber
) {
    public ReserveSeatRequest {
        Assert.notNull(concertDate, "공연 날짜는 필수입니다.");
        Assert.hasText(concertDate, "공연 날짜는 빈 값일 수 없습니다.");
        Assert.notNull(seatNumber, "좌석 번호는 필수입니다.");
        Assert.hasText(seatNumber, "좌석 번호는 빈 값일 수 없습니다.");
    }
}
