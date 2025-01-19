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
        String uuid = getUuid(request);

        try {
            // checkStatus 메서드 호출로 상태 확인
            queueFacade.checkStatus(uuid);
        }
        catch (NullPointerException e){
            // uuid 가 null 이거나 빈값일 경우
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
            return false;
        }
        catch(IllegalStateException e){
            // uuid 가 wait 일 경우 (대기 인원도 반환)
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getMessage());
            return false;
        }
        catch (Exception e) {
            // 토큰 확인 중 에러 발생 시 요청 차단
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing token: " + e.getMessage());
            return false;
        }
        // 요청 성공
        return true;
    }

    private static String getUuid(HttpServletRequest request) {
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
        return uuid;
    }
}
