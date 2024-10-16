package dormease.dormeasedev.domain.users.auth.domain.repository;

import dormease.dormeasedev.domain.users.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByLoginId(String loginId);

    // 소유자의 loginId가 파라미터로 받은 loginId면서 재발급 횟수가 파라미터로 받은 reissueCount보다 작은 RefreshToken 객체를 반환
    Optional<RefreshToken> findByLoginIdAndReissueCountLessThan(String loginId, long reissueCount);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
