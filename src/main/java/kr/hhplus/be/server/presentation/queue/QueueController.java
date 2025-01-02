package kr.hhplus.be.server.presentation.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class QueueController {
    // 토큰 발급 API
    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken() {
        return ResponseEntity.ok(Map.of("token", "123e4567-e89b-12d3-a456-426614174000"));
    }

    // 대기열 상태 확인 API
    @PostMapping("/token/status")
    public ResponseEntity<Map<String, Object>> getQueueStatus(@RequestParam String token) {
        return ResponseEntity.ok(Map.of(
                "estimatedWaitTime", 200
        ));
    }
}
