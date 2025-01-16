package kr.hhplus.be.server.common.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.application.queue.QueueFacade;
import kr.hhplus.be.server.domain.queue.QueueService;
import kr.hhplus.be.server.application.queue.dto.QueueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class QueueInterceptor implements HandlerInterceptor {

    private final QueueFacade queueFacade;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // UUID를 요청 헤더나 파라미터에서 가져옴
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("queue-uuid".equals(cookie.getName())) {
                    uuid = cookie.getValue();
                }
            }
        }

        // UUID가 없는 경우 요청 차단
        if (uuid == null || uuid.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing token UUID");
            return false;
        }

        try {
            // checkStatus 메서드 호출로 상태 확인
            QueueResponse queueResponse = queueFacade.checkStatus(uuid);

            // 토큰 상태가 "active"가 아니면 요청 차단
            if (!"active".equals(queueResponse.getIsActive())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access denied, waitingCount is : " + queueResponse.getWaitingCount());
                return false;
            }
        } catch (Exception e) {
            // 토큰 확인 중 에러 발생 시 요청 차단
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing token: " + e.getMessage());
            return false;
        }
        // 요청 성공
        return true;
    }
}
