package kr.hhplus.be.server.application.queue.dto;

import kr.hhplus.be.server.domain.queue.Queue;
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

    public void setWaitingCount(Long waitingCount) {
        this.waitingCount = waitingCount;
    }

    public static QueueResponse fromEntity(Queue queue) {
        return QueueResponse.builder()
                .isActive(queue.getIsActive())
                .expireAt(queue.getExpireAt())
                .build();
    }
}
