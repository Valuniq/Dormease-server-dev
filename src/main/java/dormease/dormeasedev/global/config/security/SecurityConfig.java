package dormease.dormeasedev.global.config.security;

import dormease.dormeasedev.global.config.security.filter.JwtAuthenticationFilter;
import dormease.dormeasedev.global.config.security.handler.JwtAccessDeniedHandler;
import dormease.dormeasedev.global.config.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// https://colabear754.tistory.com/171 참고함
@EnableMethodSecurity
//@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;	// JwtAuthenticationFilter 주입

    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", "/api/v1/auth/**"};	// sign-up, sign-in 추가

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))	// H2 콘솔 사용을 위한 설정
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                            .accessDeniedHandler(new JwtAccessDeniedHandler());
                })
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls)
                                .permitAll()	// requestMatchers의 인자로 전달된 url은 모두에게 허용
                                .requestMatchers(PathRequest.toH2Console())
                                .permitAll()	// H2 콘솔 접속은 모두에게 허용
                                .requestMatchers("/api/v1/web/**").hasRole("ADMIN")
                                .anyRequest().authenticated()	// 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	// 세션을 사용하지 않으므로 STATELESS 설정
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)	// 추가
                .build();
    }
}

