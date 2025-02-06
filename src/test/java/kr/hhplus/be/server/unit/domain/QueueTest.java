package kr.hhplus.be.server.unit.domain;


import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class QueueTest {
    @Test
    void 대기열토큰이_활성화_된다(){
        Queue queue = Queue.builder()
                .isActive(QueueStatus.waiting)
                .createdDate(LocalDateTime.now())
                .build();

        // when
        queue.activate();

        // 활성화 상태
        Assertions.assertEquals("active",queue.getIsActive());
        // 활성화 만료기간이 생긴다
        Assertions.assertNotNull(queue.getExpireAt());
    }

}
