package kr.hhplus.be.server.application.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

@Schema(description = "결제 요청 데이터")
public record PaymentRequest(
        @Schema(description = "사용자 ID", example = "1", required = true)
        Long userId,

        @Schema(description = "결제 금액", example = "30000", required = true)
        Integer amount
) {
    public PaymentRequest {
        Assert.notNull(userId, "사용자 식별자가 필요합니다.");
        Assert.notNull(amount, "충전 금액이 필요합니다.");
        Assert.isTrue(amount > 0, "충전 금액은 양수여야 합니다.");
    }
}
