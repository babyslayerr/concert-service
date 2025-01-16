package kr.hhplus.be.server.application.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserBalanceHistoryResponse {
    private Long amount;
    private String actionType;
    private Long balance;
}
