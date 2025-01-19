package kr.hhplus.be.server.presentation.queue;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.application.queue.dto.QueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QueueController {

    private final QueueFacade queueFacade;
    // 토큰 발급 API
    @Operation(summary = "토큰 발급 API")
    @PostMapping("/token")
    public ResponseEntity<String> generateToken(HttpServletResponse response) {
        String uuid = queueFacade.getToken();

        Cookie cookie = new Cookie("queue-uuid", uuid);

        response.addCookie(cookie);
        return ResponseEntity.ok(uuid);
    }

    // 대기열 상태 확인 API
    @Operation(summary = "대기열 상태 확인 API")
    @GetMapping("/token/status")
    public ResponseEntity<QueueResponse> getQueueStatus(@RequestParam(name = "uuid") String uuid) {
        QueueResponse queueResponse = queueFacade.getStatus(uuid);
        return ResponseEntity.ok(queueResponse);
    }
}
