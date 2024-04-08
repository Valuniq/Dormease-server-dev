package dormease.dormeasedev.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpRequest {

    private String loginId;
    private String password;
    private String name;
}
