package kr.hhplus.be.server.infrastructure.scheduler;

import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueRepository queueRepository;

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void expireAndActiveTokens() {
        // 1. 만료 처리
        expireTokens();
        // 2. 활성화 처리
        activateTokens();
    }

    private void expireTokens() {
        // 활성화 된 것들중에 만료시간이 지난 것
        List<Queue> expiredTokens = queueRepository.findByIsActiveAndExpireAtBefore("active",LocalDateTime.now());
        expiredTokens.forEach(queue -> {
            // 토큰 삭제
            queueRepository.delete(queue); // 상태 저장
        });
    }

    private void activateTokens() {
        long totalSlot = 100;
        long activeCount = queueRepository.countByIsActive("active");

        // 활성 토큰이 100개 미만인 경우만 활성화
        if (activeCount < totalSlot) {
            long remainingSlots = (totalSlot - activeCount);
            Pageable pageable = PageRequest.of(0, (int) remainingSlots);
            List<Queue> tokensForActive = queueRepository.findByIsActiveOrderByCreatedDateAsc("wait",pageable);
            tokensForActive.forEach(queue -> {
                queue.activate(); // 상태를 active 로 변경
                queueRepository.save(queue); // 상태 저장
            });
        }
    }
}