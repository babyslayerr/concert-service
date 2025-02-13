package kr.hhplus.be.server.common.event.listener;

import kr.hhplus.be.server.common.event.CompletedPaymentEvent;
import kr.hhplus.be.server.common.event.CompletedReservationEvent;
import kr.hhplus.be.server.common.external.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ExternalApiSender {
    private final ExternalApiClient externalApiClient;
    @TransactionalEventListener
    public void sendReservationInfo(CompletedReservationEvent event) {
        externalApiClient.sendReservationInfo(event.getReservationId(), event.getUserId(), event.getSeatId(), event.getPrice());
    }
    @TransactionalEventListener
    public void sendPaymentInfo(CompletedPaymentEvent event) {
        externalApiClient.sendPaymentInfo(event.getReservationId(), event.getUserId(), event.getSeatId(), event.getPrice());
    }
}
