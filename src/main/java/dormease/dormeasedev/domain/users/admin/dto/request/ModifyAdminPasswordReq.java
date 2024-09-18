package dormease.dormeasedev.domain.users.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ModifyAdminPasswordReq {

    @Schema(type = "String", example = "password", description= "로그인 시 사용될 비밀번호입니다. 영문 대/소문자, 숫자 및 특수문자 중 2가지 이상을 조합하여 6~20자로 입력하세요.")
    @Size(min = 6, max = 20, message = "비밀번호는 6~20자여야 합니다.")
    @Pattern(regexp = "^(?:(?=.*[A-Za-z])(?=.*\\d)|(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])|(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])).{6,20}$",
            message = "비밀번호는 영문(대/소문자), 숫자, 특수문자 중 2가지 이상을 조합하여 6~20자 사이로 입력해야 합니다.")
    private String password;
}
