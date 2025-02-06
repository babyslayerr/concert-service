package kr.hhplus.be.server.domain.queue;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.infrastructure.queue.QueueRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    public String getToken() {
        Queue token = new Queue(UUID.randomUUID().toString());
        log.info("create token uuid: {}",token.getUuid());
        return queueRepository.save(token).getUuid();
    }

    public Queue checkStatus(String uuid) {
        // UUID가 없는 경우 요청 차단
        if (uuid == null || uuid.isEmpty()) {
            throw new NullPointerException("Missing token UUID");
        }
        Queue queue = queueRepository.findByUuid(uuid).orElseThrow();

        // 만약 active 상태면 그대로 반환
        return queue;
    }

    // 대기순번 계산
    public long getWaitingCount(Queue queue) {
        return queueRepository.countBeforeWaitingToken(queue.getUuid());
    }

    public void removeToken(String tokenUuid) {
        Queue queue = queueRepository.findByUuid(tokenUuid).orElseThrow();
        queueRepository.delete(queue);
    }

    // 토큰 만료 처리
    public void expireTokens() {
        // 활성화 된 것들중에 만료시간이 지난 것
        // 30분이 지나면 만료
        int standardMinute = 30 * 60 * 1000;
        long deleteCount = queueRepository.deleteExpiredTokens(standardMinute);
        log.info("expire token count: {}",deleteCount);
    }

    // 토큰 활성화 처리
    public void activateTokens() {
        long totalSlot = 100;
        long activeCount = queueRepository.countByIsActive();

        // 활성 토큰이 100개 미만인 경우만 활성화
        if (activeCount < totalSlot) {
            long remainingSlots = (totalSlot - activeCount);
            long activatedCount = queueRepository.addActiveTokens(remainingSlots);
            log.info("active token count: {}",activatedCount);
        }
    }

    // 토큰 활성화 처리(오버로딩 놀이공원 방식에 사용)
    public void activateTokens(long activeCount) {
        long activatedCount = queueRepository.addActiveTokens(activeCount);
        log.info("active token count: {}",activatedCount);
    }
}
