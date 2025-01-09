package kr.hhplus.be.server.presentation.queue;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class QueueResponse {

    private String isActive;

    private Long waitingCount;
    // active 상태시 만료 시점
    private LocalDateTime expireAt;
}
