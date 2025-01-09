package kr.hhplus.be.server.presentation.user.dto;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserBalanceHistoryResponse {
    private Long amount;
    private String actionType;
    private Long balance;
}
