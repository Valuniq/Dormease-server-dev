package dormease.dormeasedev.global.security;

import dormease.dormeasedev.domain.users.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long userId;

    private final String loginId;

    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    @Builder
    public UserDetailsImpl(Long userId, String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl of(User user) {
        // Assuming User has a getRole() method that returns a Role object or similar
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(user.getUserType().getKey())); // key: ROLE_권한
        List<GrantedAuthority> authorities = List.of(user.getUserType()) // Replace with actual role retrieval logic
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getKey())) // Replace with actual role key retrieval logic
                .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
