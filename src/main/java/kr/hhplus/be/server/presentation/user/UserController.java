package kr.hhplus.be.server.presentation.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    // 잔액 충전 API
    @PostMapping("/payment/charge")
    public ResponseEntity<Map<String, Object>> chargeBalance(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
                "userId", request.get("userId"),
                "balance", 5000,
                "message", "Balance charged successfully."
        ));
    }

    // 잔액 조회 API
    @GetMapping("/payment/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam int userId) {
        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "balance", 3000
        ));
    }
}
