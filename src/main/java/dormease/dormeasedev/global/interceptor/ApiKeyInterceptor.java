package dormease.dormeasedev.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${example.verify.key}")
    private String MYONG_JI_API_KEY;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("ApiKey Interceptor Start");
        String apiKey = request.getParameter("key");

        if (MYONG_JI_API_KEY.equals(apiKey)) {
            return true; // API Key가 다음 로직으로
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false; // API Key가 유효하지 않으면 요청을 중단
        }
    }
}
