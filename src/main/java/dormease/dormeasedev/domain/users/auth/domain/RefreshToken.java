package dormease.dormeasedev.domain.users.auth.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.users.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RefreshToken extends BaseEntity {

    @Id
    @Column(name = "login_id" ,nullable = false)
    private String loginId;

//    @MapsId - 없어도 되는듯?
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    // 재발급 횟수를 제한할 것이기 때문
    private int reissueCount = 0;

    // Refresh Token 업데이트 함수
    public RefreshToken updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    // Refresh Token 유효성 체크 함수
    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    // 재발급 횟수 카운트 함수
    public void increaseReissueCount() {
        reissueCount++;
    }

    @Builder
    public RefreshToken(String loginId, User user, String refreshToken) {
        this.loginId = loginId;
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
