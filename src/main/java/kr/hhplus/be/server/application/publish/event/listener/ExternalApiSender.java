package kr.hhplus.be.server.application.publish.event.listener;

import kr.hhplus.be.server.application.publish.event.CompletedPaymentEvent;
import kr.hhplus.be.server.application.publish.event.CompletedReservationEvent;
import kr.hhplus.be.server.infrastructure.external.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ExternalApiSender {
    private final ExternalApiClient externalApiClient;
    @Async
    @TransactionalEventListener
    public void sendReservationInfo(CompletedReservationEvent event) {
        externalApiClient.sendReservationInfo(event.getReservationId(), event.getUserId(), event.getSeatId(), event.getPrice());
    }
    @Async
    @TransactionalEventListener
    public void sendPaymentInfo(CompletedPaymentEvent event) {
        externalApiClient.sendPaymentInfo(event.getReservationId(), event.getUserId(), event.getSeatId(), event.getPrice());
    }
}
