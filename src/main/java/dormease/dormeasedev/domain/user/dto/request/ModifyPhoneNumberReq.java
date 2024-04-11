package dormease.dormeasedev.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ModifyPhoneNumberReq {

    @Schema(type = "String", example = "01012345678", description= "사용자의 전화번호입니다.")
    private String phoneNumber;
}
