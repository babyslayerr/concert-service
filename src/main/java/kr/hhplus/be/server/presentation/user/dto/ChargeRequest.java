package kr.hhplus.be.server.presentation.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

public record ChargeRequest(
        @Schema(description = "사용자의 ID", example = "1", required = true)
        Long userId,

        @Schema(description = "충전할 금액", example = "30000", required = true)
        Long amount
) {
    public ChargeRequest {
        Assert.notNull(userId, "사용자 식별자가 필요합니다.");
        Assert.notNull(amount, "충전 금액이 필요합니다.");
    }
}
