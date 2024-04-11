package dormease.dormeasedev.global.config.security.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dormease.dormeasedev.domain.auth.domain.RefreshToken;
import dormease.dormeasedev.domain.auth.domain.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class TokenProvider {

    private final String secretKey;
    private final long expirationMinutes;
    private final long refreshExpirationHours;
    private final String issuer;
    private final long reissueLimit;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public TokenProvider(
            @Value("${secret-key}") String secretKey,
            @Value("${expiration-minutes}") long expirationMinutes,
            @Value("${refresh-expiration-hours}") long refreshExpirationHours,
            @Value("${issuer}") String issuer,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.secretKey = secretKey;
        this.expirationMinutes = expirationMinutes;
        this.refreshExpirationHours = refreshExpirationHours;
        this.issuer = issuer;
        this.refreshTokenRepository = refreshTokenRepository;
        this.reissueLimit = refreshExpirationHours * 60 / expirationMinutes; // 재발급 한도
    }

    public String createAccessToken(String userSpecification) {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(userSpecification)  // JWT 토큰 제목
                .setIssuer(issuer)  // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))    // JWT 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    // 기존 액세스 토큰을 토대로 새로운 액세스 토큰을 생성
    @Transactional
    public String recreateAccessToken(String oldAccessToken) throws JsonProcessingException {
        String subject = decodeJwtPayloadSubject(oldAccessToken);
        refreshTokenRepository.findByLoginIdAndReissueCountLessThan(subject.split(":")[0], reissueLimit)
                .ifPresentOrElse(
                        RefreshToken::increaseReissueCount,
                        () -> { throw new ExpiredJwtException(null, null, "Refresh token expired."); }
                );
        return createAccessToken(subject);
    }

    public String validateTokenAndGetSubject(String token) {
        return validateAndParseToken(token)
                .getBody()
                .getSubject();
    }

    // 리프레시 토큰이 유효한 토큰인지를 검증
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        validateAndParseToken(refreshToken);
        String loginId = decodeJwtPayloadSubject(oldAccessToken).split(":")[0];
        refreshTokenRepository.findByLoginIdAndReissueCountLessThan(loginId, reissueLimit)
                .filter(userRefreshToken -> userRefreshToken.validateRefreshToken(refreshToken))
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh token expired."));
    }

    // 기존의 validateTokenAndGetSubject()에서 분리한 메소드
    // 내부의 parseCliamJws()에서 JWT를 파싱할 때, 토큰이 유효한지 검사하여 예외를 던지기 때문에 토큰 검증에 사용
    private Jws<Claims> validateAndParseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);
    }

    // JWT를 복호화하고 데이터가 담겨있는 Payload에서 Subject를 반환.
    // Subject에는 회원ID와 회원 타입이 문자열 형태로 담겨있다.
    // 이미 만료된 액세스 토큰을 복호화할 것이기 때문에 유효한 토큰인지는 검사하지 않는다.
    // 유효시간 만료가 아닌 이유로 무효한 토큰이라면 해당 메소드를 호출하지 않을 것이다.
    private String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
//                .setIssuer(issuer)
//                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    // 비밀키를 토대로 createToken()에서 토큰에 담은 Subject를 복호화하여 문자열 형태로 반환하는 메소드
//    public String validateTokenAndGetSubject(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//
////        try {
////            return Jwts.parserBuilder()
////                    .setSigningKey(secretKey.getBytes())
////                    .build()
////                    .parseClaimsJws(token)
////                    .getBody()
////                    .getSubject();
////        } catch (io.jsonwebtoken.security.SecurityException ex) {
////            log.error("잘못된 JWT 서명입니다.");
////        } catch (MalformedJwtException ex) {
////            log.error("잘못된 JWT 서명입니다.");
////        } catch (ExpiredJwtException ex) {
////            log.error("만료된 JWT 토큰입니다.");
////        } catch (UnsupportedJwtException ex) {
////            log.error("지원되지 않는 JWT 토큰입니다.");
////        } catch (IllegalArgumentException ex) {
////            log.error("JWT 토큰이 잘못되었습니다.");
////        }
////        return null;
//    }

}
