package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.domain.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueFacade {

    private final QueueService queueService;
}
