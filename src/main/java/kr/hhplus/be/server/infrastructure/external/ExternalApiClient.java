package kr.hhplus.be.server.infrastructure.external;

import org.springframework.stereotype.Component;

// event -> kafka 로 구현이 변경되면서 API Call 이 아닌 발행 구독 시스템으로 변경
// 되면서 deprecated
@Deprecated
@Component
public class ExternalApiClient {
    public void sendReservationInfo(Long reservationId, Long userId, Long seatId, Long price) {
        System.out.printf("외부 API 호출 - 예약 ID: %d, 유저 ID: %d, 좌석 ID: %d, 가격: %d%n",
                reservationId, userId, seatId, price);
    }

    public void sendPaymentInfo(Long reservationId, Long userId, Long seatId, Long price) {
        System.out.printf("외부 API 호출 - 예약 ID: %d, 유저 ID: %d, 좌석 ID: %d, 가격: %d%n",
                reservationId, userId, seatId, price);
    }
}
