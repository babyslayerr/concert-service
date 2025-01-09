package kr.hhplus.be.server.presentation.queue;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QueueResponse {

    private String isActive;

    private Long waitingCount;
}
