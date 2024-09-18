package dormease.dormeasedev.domain.users.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CheckSecurityCodeReq {

    @Schema(type = "String", example = "password", description= "로그인 시 사용될 비밀번호입니다. 영문 대/소문자, 숫자 및 특수문자 중 2가지 이상을 조합하여 6~20자로 입력하세요.")
    @Size(min = 8, max = 8, message = "보안 코드는 8자여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]{8}$", message = "보안 코드는 영어 소문자와 숫자로만 구성된 8자리여야 합니다.")
    private String securityCode;
}
