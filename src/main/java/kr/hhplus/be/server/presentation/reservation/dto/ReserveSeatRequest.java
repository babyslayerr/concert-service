package kr.hhplus.be.server.presentation.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

@Schema(description = "좌석 예약 요청 데이터")
public record ReserveSeatRequest(
        @Schema(description = "유저 ID", example = "1",required = true)
        Long userId,

        @Schema(description = "공연스케줄 ID" , example = "1", required = true)
        Long concertScheduleId,

        @Schema(description = "좌석 번호", example = "12", required = true)
        Long seatNumber
) {
    public ReserveSeatRequest {
        Assert.notNull(userId, "유저 ID는 필수입니다.");
        Assert.notNull(concertScheduleId, "공연 스케줄은 필수입니다.");
        Assert.notNull(seatNumber, "좌석 번호는 필수입니다.");
    }
}
