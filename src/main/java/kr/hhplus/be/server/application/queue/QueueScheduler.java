package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.domain.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueService queueService;

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void expireAndActiveTokens() {
        // 1. 토큰 만료 처리
        queueService.expireTokens();
        // 2. 토큰 활성화 처리
        queueService.activateTokens();
    }


}