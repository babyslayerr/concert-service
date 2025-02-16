package kr.hhplus.be.server.infrastructure.external;

import org.springframework.stereotype.Component;

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
