package dormease.dormeasedev.domain.users.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResetPasswordReq {

    @Schema(type = "String", example = "password", description= "새로운 비밀번호입니다.")
    private String password;
}
