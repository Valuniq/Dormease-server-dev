package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DepositDormitoryApplicaionRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "홍길동", description = "학생 이름")
    private String studentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "Gender(Enum)", example = "MALE", description = "성별")
    private Gender gender;

    @Schema(type = "DormitoryRoomTypeRes", description = "선발 결과 기숙사(인실/성별 구분)에 대한 정보입니다.")
    private DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes;

    @Schema(type = "DormitoryApplicationResult(Enum)", example = "WAIT", description = "합격 여부. PASS(합격) / NON_PASS(불합격) / MOVE_PASS(이동 합격) / WAIT(대기: 아직 결과 x) 中 1.")
    private DormitoryApplicationResult dormitoryApplicationResult;
}
