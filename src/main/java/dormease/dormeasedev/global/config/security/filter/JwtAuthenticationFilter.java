package dormease.dormeasedev.global.config.security.filter;

import dormease.dormeasedev.global.config.security.token.CustomUserDetailService;
import dormease.dormeasedev.global.config.security.token.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

// 토큰에 담긴 정보로 사용자를 필터링해줄 필터
@Slf4j
@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;

    // 요청 마다 필터링
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION); // Access Token 추출
        String refreshToken = parseBearerToken(request, "Refresh-Token"); // Refresh Token 추출

        try {
            if (accessToken != null && tokenProvider.validateTokenAndGetSubject(accessToken) != null) {
                // Access Token이 유효한 경우
//                User user = parseUserSpecification(accessToken);
                UserDetails userDetails = parseUserSpecification(accessToken);
//                AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, accessToken, user.getAuthorities());
                AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(userDetails, accessToken, userDetails.getAuthorities());
                authenticated.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } else if (refreshToken != null) {
                // Refresh Token이 있는 경우
                reissueAccessToken(request, response, refreshToken);
                return; // 요청을 처리하고 종료
            }

        } catch (ExpiredJwtException e) {	// 변경
            reissueAccessToken(request, response, refreshToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    // 매개변수에 헤더이름 추가
    private String parseBearerToken(HttpServletRequest request, String headerName) {
        return Optional.ofNullable(request.getHeader(headerName))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

//    private User parseUserSpecification(String token) {
    private UserDetails parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        // split[0] ==> loginId
        UserDetails userDetails = customUserDetailService.loadUserByUsername(split[0]);

        //

//        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
        return userDetails;
    }

    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        try {
            String oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION);

            tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);

            String newAccessToken = tokenProvider.recreateAccessToken(oldAccessToken);

//            User user = parseUserSpecification(newAccessToken);
            UserDetails userDetails = parseUserSpecification(newAccessToken);
//            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.getAuthorities());
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(userDetails, newAccessToken, userDetails.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            response.setHeader("New-Access-Token", newAccessToken); // 응답 헤더에 새로운 Access Token 추가
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
    }

    // for Userprincipal
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            log.info("bearerToken = {}", bearerToken.substring(7, bearerToken.length()));
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
