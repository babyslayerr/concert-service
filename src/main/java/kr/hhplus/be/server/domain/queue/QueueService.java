package kr.hhplus.be.server.domain.queue;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.queue.dto.QueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    @Transactional
    public String getToken() {
        Queue token = Queue.builder()
                .uuid(UUID.randomUUID().toString())
                .build();

        return queueRepository.save(token).getUuid();
    }

    @Transactional
    public Queue checkStatus(String uuid) {
        Queue queue = queueRepository.findByUuid(uuid).orElseThrow();

        // 만약 active 상태면 그대로 반환
        return queue;
    }

    // 대기순번 계산
    public long getWaitingCount(Queue queue) {
        return queueRepository.countByCreatedDateBeforeAndIsActive(queue.getCreatedDate(), "wait");
    }

    public void removeToken(String tokenUuid) {
        Queue queue = queueRepository.findByUuid(tokenUuid).orElseThrow();
        queueRepository.delete(queue);
    }

    // 토큰 만료 처리
    public void expireTokens() {
        // 활성화 된 것들중에 만료시간이 지난 것
        List<Queue> expiredTokens = queueRepository.findByIsActiveAndExpireAtBefore("active", LocalDateTime.now());
        expiredTokens.forEach(queue -> {
            // 토큰 삭제
            queueRepository.delete(queue); // 상태 저장
        });
    }

    // 토큰 활성화 처리
    public void activateTokens() {
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
