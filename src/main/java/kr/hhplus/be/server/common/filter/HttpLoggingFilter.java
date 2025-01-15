package kr.hhplus.be.server.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청 정보 추출
        String method = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();

        // 요청 시작 시간
        long startTime = System.currentTimeMillis();

        // 필터 체인 진행
        chain.doFilter(request, response);

        // 응답 정보 추출
        int status = httpResponse.getStatus();

        // 응답 시간
        long endTime = System.currentTimeMillis();
        // 총 요청-응답 시간
        long latency = endTime - startTime;

        // 하나의 로그에 요청과 응답 정보를 합쳐 출력
        log.info("HTTP Request-Response: [{} {}] -> [{}] ({} ms)",
                method,
                requestURI,
                status,
                latency
        );
    }
}
