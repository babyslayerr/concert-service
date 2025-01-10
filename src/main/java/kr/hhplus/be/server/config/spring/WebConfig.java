package kr.hhplus.be.server.config.spring;

import kr.hhplus.be.server.common.interceptor.QueueInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final QueueInterceptor queueInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터 등록 및 경로 설정
        registry.addInterceptor(queueInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/token/status", "/token","/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"); // 토큰 발급 및 상태 확인에 대해서는 예외
    }
}