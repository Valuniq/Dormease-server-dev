package dormease.dormeasedev.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpRequest {

    @Schema( type = "String", example = "phonil", description= "로그인 시 사용될 아이디입니다.")
    private String loginId;

    @Schema( type = "String", example = "password", description= "로그인 시 사용될 비밀번호입니다.")
    private String password;

    private String name;
}
