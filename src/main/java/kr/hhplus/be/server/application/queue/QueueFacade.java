package kr.hhplus.be.server.application.queue;

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
    public QueueResponse checkStatus(String uuid){

        Queue queue = queueService.checkStatus(uuid);
        QueueResponse queueResponse = QueueResponse.fromEntity(queue);
        
        // wait 상태일경우 대기자인원수 추가
        if(queueResponse.getIsActive().equals("wait")){
            long waitingCount = queueService.getWaitingCount(queue);
            queueResponse.setWaitingCount(waitingCount);
        }

        return queueResponse;
    }

    @Transactional
    public String getToken(){
        String token = queueService.getToken();
        return token;
    }
}
