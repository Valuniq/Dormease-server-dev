package dormease.dormeasedev.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignInRequest {

    private String loginId;
    private String password;
}
