package kr.hhplus.be.server.application.queue;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.queue.dto.QueueResponse;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueFacade {

    private final QueueService queueService;

    @Transactional
    public QueueResponse getStatus(String uuid){

        Queue queue = queueService.checkStatus(uuid);
        QueueResponse queueResponse = QueueResponse.fromEntity(queue);

        // wait 상태일경우 대기자인원수 추가
        if(queueResponse.isWaiting()){
            long waitingCount = queueService.getWaitingCount(queue);
            queueResponse.setWaitingCount(waitingCount);
        }
        return queueResponse;
    }

    // interceptor 용 토큰 확인 파사드(active 가 아니면 에러)
    @Transactional
    public void checkStatus(String uuid){

        Queue queue = queueService.checkStatus(uuid);

        // wait 상태일경우 대기자인원수 추가
        if(queue.isWaiting()){
            long waitingCount = queueService.getWaitingCount(queue);
            throw new IllegalStateException("Access denied, waitingCount is : " + waitingCount);
        }

    }

    @Transactional
    public String getToken(){
        String token = queueService.getToken();
        return token;
    }
}
