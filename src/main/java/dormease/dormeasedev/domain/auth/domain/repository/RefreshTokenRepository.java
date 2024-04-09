package dormease.dormeasedev.domain.auth.domain.repository;

import dormease.dormeasedev.domain.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    // 소유자의 loginId가 파라미터로 받은 loginId면서 재발급 횟수가 파라미터로 받은 reissueCount보다 작은 RefreshToken 객체를 반환
    Optional<RefreshToken> findByLoginIdAndReissueCountLessThan(String loginId, long reissueCount);
}
