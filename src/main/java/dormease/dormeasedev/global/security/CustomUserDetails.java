package dormease.dormeasedev.global.security;

import dormease.dormeasedev.domain.users.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private Long id;

    private String loginId;

    private String password;

    // -----------
    private boolean emailVerified;	//이메일 인증 여부
    private boolean locked;	//계정 잠김 여부
    private String nickname;	//닉네임
//    private Collection<GrantedAuthority> authorities;	//권한 목록
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    public CustomUserDetails(Long id, String loginId, String password, boolean emailVerified, boolean locked, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.emailVerified = emailVerified;
        this.locked = locked;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    public CustomUserDetails(Long id, String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetails create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getUserType().getValue()));
        return new CustomUserDetails(
                user.getId(),
                user.getLoginId(),
                user.getPassword(),
                authorities
        );
    }

     // 해당 유저의 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

     // 비밀번호
    @Override
    public String getPassword() {
        return password;
    }


     // PK값
    @Override
    public String getUsername() {
        return this.getLoginId();
    }

    /**
     * 계정 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    /**
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * 사용자 활성화 여부
     * ture : 활성화
     * false : 비활성화
     * @return
     */
    @Override
    public boolean isEnabled() {
        //이메일이 인증되어 있고 계정이 잠겨있지 않으면 true
        return (emailVerified && !locked);
    }

}
