package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PassDormitoryApplicationRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "홍길동", description = "학생 이름")
    private String studentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "Gender(Enum)", example = "MALE", description = "성별")
    private Gender gender;

    @Schema(type = "boolean", example = "true", description = "흡연 여부")
    private boolean smoker;

    @Schema(type = "String", example = "abcd1234", description = "룸메이트 신청 코드")
    private String roommateCode;
}
