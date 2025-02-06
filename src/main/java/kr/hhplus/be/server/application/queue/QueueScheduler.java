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

    // 은행창구 방식 스케줄러
//    @Scheduled(fixedRate = 5000) // 5초마다 실행
//    @Transactional
//    public void expireAndActiveTokens() {
//        // 1. 토큰 만료 처리
//        queueService.expireTokens();
//        // 2. 토큰 활성화 처리
//        queueService.activateTokens();
//    }
    // 놀이공원 방식
    // 만료와, 활성화는 상관관계가 없으므로 별도 분리
    @Scheduled(fixedRate = 10000) // 10초마다 실행
    @Transactional
    public void activeTokens(){
        // 6000명 활성화
        long activeCount = 6000;
        queueService.activateTokens(activeCount);
    }

    // 만료처리
    @Scheduled(fixedRate = 20000) // 20초마다 실행
    @Transactional
    public void expireTokens(){
        queueService.expireTokens();
    }
}