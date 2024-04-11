package dormease.dormeasedev.domain.auth.dto.request;

import dormease.dormeasedev.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpReq {

    @Schema(type = "Long", example = "1", description = "학교 id입니다.")
    @NotNull(message = "학교 id를 입력해야 합니다.")
    private Long schoolId;

    @Schema(type = "String", example = "강승우", description= "사용자의 실제 이름입니다.")
    private String name;

    @Schema(type = "Gender", example = "MALE", description = "성별입니다. MALE, FEMALE, EMPTY 中 1")
    private Gender gender;

    @Schema(type = "String", example = "01012345678", description= "사용자의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "String", example = "60190927", description= "사용자의 학번입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "phonil", description= "로그인 시 사용될 아이디입니다. 중복 x.")
    private String loginId;

    @Schema(type = "String", example = "password", description= "로그인 시 사용될 비밀번호입니다. 영문 대/소문자, 숫자 및 특수문자 중 2가지 이상을 조합하여 6~20자로 입력하세요.")
//    @Size(min = 6, max = 20, message = "비밀번호는 6~20자여야 합니다.")
//    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])[a-zA-Z0-9]{2,10}",
//            message = "비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2가지 이상을 조합하여야 합니다.")
    private String password;

    // 아래는 msi 인증 후 가능
//    private SchoolStatus schoolStatus;
//
//    // 거주지
//    private String address;
//
//    // 학과
//    private String major;
//
//    // 학년
//    private Integer grade;

}
