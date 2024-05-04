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
public class DormitoryApplicationDetailRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    @Schema(type = "String", example = "2024학년도 1학기 1차 정기 신청", description = "입사 신청 설정 제목")
    private String dormitoryApplicationSettingTitle;

    @Schema(type = "String", example = "명지대학교 자연캠퍼스", description = "학교명")
    private String schoolName;

    @Schema(type = "String", example = "4동", description = "기숙사명")
    private String dormitoryName;

    @Schema(type = "Gender(Enum)", example = "MALE", description = "성별")
    private Gender gender;

    @Schema(type = "Integer", example = "4", description = "인실")
    private Integer roomSize;

    @Schema(type = "String", example = "6개월", description = "거주 기간")
    private String term;

    @Schema(type = "Integer", example = "100(식)", description = "식권 개수. 숫자만 응답합니다.")
    private Integer mealTicketCount;

    @Schema(type = "Boolean", example = "true", description = "우선 선발 증빙 서류 제출 여부")
    private Boolean prioritySelectionCopy;

    @Schema(type = "Boolean", example = "true", description = "등본 제출 여부")
    private Boolean copy;

    @Schema(type = "Boolean", example = "true", description = "흡연 여부")
    private Boolean smoking;

    @Schema(type = "Integer", example = "30000", description = "보증금")
    private Integer securityDeposit;

    @Schema(type = "Integer", example = "1200000", description = "기숙사비 + 식권")
    private Integer dormitoryPlusMealTicketPrice;

    @Schema(type = "Integer", example = "1230000", description = "총액")
    private Integer totalPrice;

    @Schema(type = "String", example = "010-1234-5678", description = "비상 연락처")
    private String emergencyContact;

    @Schema(type = "String", example = "친구", description = "비상 연락처와의 관계")
    private String emergencyRelation;

    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    @Schema(type = "String", example = "00000000000", description = "계좌 번호")
    private String accountNumber;

    @Schema(type = "DormitoryApplicationResult(Enum)", example = "WAIT", description = "신청 결과. PASS(합격) / NON_PASS(불합격) / MOVE_PASS(이동 합격) / WAIT(대기: 아직 결과 x) 中 1")
    private DormitoryApplicationResult dormitoryApplicationResult;
}
