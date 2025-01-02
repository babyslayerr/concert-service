package kr.hhplus.be.server.presentation.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class ReservationController {


    // 좌석 예약 요청 API
    @PostMapping("/concert/reserve")
    public ResponseEntity<Map<String, Object>> reserveSeat(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
                "concertDate", "2024-01-01",
                "seatNumber", request.get("seatNumber"),
                "status", "reserved",
                "expireAt", LocalDateTime.now().plusMinutes(5).toString()
        ));
    }

    // 결제 API
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> makePayment(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
                "userId", request.get("userId"),
                "status", "completed",
                "message", "성공적으로 결제가 완료 되었습니다."
        ));
    }
}
