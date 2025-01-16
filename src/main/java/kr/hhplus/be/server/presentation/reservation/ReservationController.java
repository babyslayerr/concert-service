package kr.hhplus.be.server.presentation.reservation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.application.reservation.ReservationFacade;
import kr.hhplus.be.server.application.reservation.dto.ReservationResponse;
import kr.hhplus.be.server.presentation.reservation.dto.PaymentRequest;
import kr.hhplus.be.server.presentation.reservation.dto.ReserveSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    // 좌석 예약 요청 API
    @Operation(summary = "좌석 예약 요청 API")
    @PostMapping("/concert/reserve")
    public ResponseEntity<ReservationResponse> reserveSeat(@RequestBody ReserveSeatRequest reserveSeatRequest) {
        ReservationResponse response = reservationFacade.reserveSeat(reserveSeatRequest.userId(), reserveSeatRequest.concertScheduleId(), reserveSeatRequest.seatNumber());

        return ResponseEntity.ok(response);
    }

    // 결제 API
    @Operation(summary = "결제 API")
    @PostMapping("/payment")
    public ResponseEntity<String> makePayment(HttpServletRequest request, @RequestBody PaymentRequest paymentRequest) {
        // 대기열 토큰 만료를 위한 UUID 추출
        String uuid = extractQueueUUID(request);

        reservationFacade.makeSeatPayment(paymentRequest.userId(),paymentRequest.concertSeatId(),paymentRequest.reservationId(),uuid);
        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
    }

    private static String extractQueueUUID(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("queue-uuid".equals(cookie.getName())) {
                    uuid = cookie.getValue();
                }
            }
        }
        return uuid;
    }


}
