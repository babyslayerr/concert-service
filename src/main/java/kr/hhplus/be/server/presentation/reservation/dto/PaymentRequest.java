package kr.hhplus.be.server.presentation.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

@Schema(description = "결제 요청 데이터")
public record PaymentRequest(
        @Schema(description = "사용자 ID", example = "1", required = true)
        Long userId,
        @Schema(description = "콘서트좌석 식별자", example = "1", required = true)
        Long concertSeatId,
        @Schema(description = "예약 식별자",example = "1",required = true)
        Long reservationId
) {
    public PaymentRequest {
        Assert.notNull(userId, "사용자 식별자가 필요합니다.");
        Assert.notNull(concertSeatId, "콘서트좌석 식별자가 필요합니다.");
        Assert.notNull(reservationId, "예약 식별자가 필요합니다.");
    }
}
