package kr.hhplus.be.server.presentation.reservation;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.reservation.dto.PaymentRequest;
import kr.hhplus.be.server.application.reservation.dto.ReserveSeatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class ReservationController {


    // 좌석 예약 요청 API
    @Operation(summary = "좌석 예약 요청 API")
    @PostMapping("/concert/reserve")
    public ResponseEntity<Map<String, Object>> reserveSeat(@RequestBody ReserveSeatRequest reserveSeatRequest) {
        return ResponseEntity.ok(Map.of(
                "concertDate", "2024-01-01",
                "seatNumber", reserveSeatRequest.seatNumber(),
                "status", "reserved",
                "expireAt", LocalDateTime.now().plusMinutes(5).toString()
        ));
    }

    // 결제 API
    @Operation(summary = "결제 API")
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> makePayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(Map.of(
                "userId", paymentRequest.userId(),
                "status", "completed",
                "message", "성공적으로 결제가 완료 되었습니다."
        ));
    }


}
