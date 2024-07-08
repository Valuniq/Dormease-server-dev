package dormease.dormeasedev.domain.dormitory_application.dto.response;

import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationUserRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "홍길동", description = "신청 회원 이름")
    private String name;

    @Schema(type = "String", example = "60190000", description = "신청 회원 학번")
    private String studentNumber;

    @Schema(type = "Gender", example = "MALE", description = "신청 회원 성별. MALE / FEMALE / EMPTY 中 1")
    private Gender gender;

    @Schema(type = "String", example = "명덕관", description = "신청 건물 이름")
    private String applicationDormitoryName;

    @Schema(type = "Integer", example = "2(인실)", description = "신청 건물 인실")
    private Integer applicationDormitoryRoomSize;

    @Schema(type = "String", example = "부산광역시 강서구 ...", description = "본거주지")
    private String address;

    @Schema(type = "String", example = "example.pdf", description = "등본 파일")
    private String copy;

    @Schema(type = "String", example = "example.pdf", description = "우선 선발 증빙 서류")
    private String prioritySelectionCopy;

    @Schema(type = "String", example = "3동", description = "배정 건물 이름")
    private String resultDormitoryName;

    @Schema(type = "Integer", example = "4(인실)", description = "배정 건물 인실")
    private Integer resultDormitorySize;

    @Schema(type = "DormitoryApplicationResult", example = "PASS", description = "합격 여부. PASS / NON_PASS / MOVE_PASS / WAIT 中 1")
    private DormitoryApplicationResult dormitoryApplicationResult;
}
