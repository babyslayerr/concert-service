package kr.hhplus.be.server.presentation.user;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.presentation.user.dto.ChargeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    // 잔액 충전 API
    @Operation(summary = "잔액 충전 API")
    @PostMapping("/payment/charge")
    public ResponseEntity<Map<String, Object>> chargeBalance(@RequestBody ChargeRequest chargeRequest) {

        return ResponseEntity.ok(Map.of(
                "userId", chargeRequest.userId(),
                "balance", 5000,
                "message", "Balance charged successfully."
        ));
    }

    // 잔액 조회 API
    @Operation(summary = "잔액 조회 API")
    @GetMapping("/payment/balance/{userId}")
    public ResponseEntity<Map<String, Object>> getBalance(
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "balance", 3000
        ));
    }

}
