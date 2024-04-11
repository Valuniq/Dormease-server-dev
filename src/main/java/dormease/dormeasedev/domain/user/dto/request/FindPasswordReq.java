package dormease.dormeasedev.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindPasswordReq {

    @Schema(type = "String", example = "phonil", description= "로그인 아이디입니다.")
    private String loginId;

    @Schema(type = "String", example = "password", description= "새로운 비밀번호입니다.")
    private String password;

}
