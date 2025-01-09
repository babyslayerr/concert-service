package kr.hhplus.be.server.domain.queue;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.presentation.queue.QueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    @Transactional
    public Queue getToken() {
        Queue token = Queue.builder()
                .uuid(UUID.randomUUID().toString())
                .build();

        return queueRepository.save(token);
    }

    @Transactional
    public QueueResponse checkStatus(String uuid) {
        // 변경은 클라이언트쪽이 더많을거라 생각해 책임분리를 위해 Model 에다가 mapper 메서드 작성
        Queue queue = queueRepository.findByUuid(uuid).orElseThrow();

        // 만약 active 상태면 그대로 반환
        if(queue.getIsActive().equals("active")){
            return QueueResponse
                    .builder()
                    .isActive("active")
                    .build();
        }

        // 앞선 대기자들 수
        long beforeCount = queueRepository.countByCreatedDateBefore(queue.getCreatedDate());


        return QueueResponse
                .builder()
                .isActive("wait")
                .waitingCount(beforeCount)
                .build();
    }

    public void removeToken(String tokenUuid) {
        Queue queue = queueRepository.findByUuid(tokenUuid).orElseThrow();
        queueRepository.delete(queue);
    }
}
