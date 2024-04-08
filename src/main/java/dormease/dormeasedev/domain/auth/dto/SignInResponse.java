package dormease.dormeasedev.domain.auth.dto;

import dormease.dormeasedev.domain.user.domain.UserType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SignInResponse {

    private String loginId;
    private UserType userType;
    private String accessToken;
}
