package kr.hhplus.be.server.application.publish.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompletedPaymentEvent {
    private final Long reservationId;
    private final Long userId;
    private final Long seatId;
    private final Long price;
}
