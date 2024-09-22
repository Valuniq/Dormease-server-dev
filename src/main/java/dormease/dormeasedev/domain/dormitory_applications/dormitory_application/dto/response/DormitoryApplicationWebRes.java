package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryRoomTypeRes;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationWebRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "홍길동", description = "학생 이름")
    private String studentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "Gender(Enum)", example = "MALE", description = "성별")
    private Gender gender;

    @Schema(type = "DormitoryRoomTypeRes", description = "학생이 신청한 기숙사(인실/성별 구분)에 대한 정보입니다.")
    private DormitoryRoomTypeRes applicationDormitoryRoomTypeRes;

    // TODO : 거주지 (방식 생각)
    @Schema(type = "String", example = "서울특별시 서대문구 ...", description= "사생의 주소입니다.")
    private String address;

    @Schema(type = "String", example = "helloiamcopy.pdf", description = "등본 파일")
    private String copy;

    @Schema(type = "String", example = "helloiamprioritySelectionCopy.pdf", description = "우선 선발 증빙 서류")
    private String prioritySelectionCopy;

//    @Schema(type = "DormitoryRoomTypeRes", description = "선발 결과 기숙사(인실/성별 구분)에 대한 정보입니다.")
//    private DormitoryRoomTypeRes resultDormitoryRoomTypeRes;

    @Schema(type = "DormitoryApplicationResult(Enum)", example = "WAIT", description = "신청 결과. PASS(합격) / NON_PASS(불합격) / MOVE_PASS(이동 합격) / WAIT(대기: 아직 결과 x) 中 1")
    private DormitoryApplicationResult dormitoryApplicationResult;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public class DormitoryRoomTypeRes { // TODO : 제대로 되는지 고민

        @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
        private String dormitoryName;

        @Schema(type = "String", example = "4인실", description = "인실")
        private Integer roomSize;
    }
}
