package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DormitoryApplicationReq {

    @Schema(type = "Long", example = "1", description= "기숙사(인실 구분)과 거주기간을 연결짓는 테이블의 ID입니다. 가격이 포함되어 있습니다.")
    private Long dormitoryTermId;

    @Schema(type = "Long", example = "1", description = "식권 ID")
    private Long mealTicketId;

    // 등본
    @Schema(type = "String", example = "helloiamcopy.pdf", description = "등본")
    private String copy;

    // 우선 선발 증빙 서류
    @Schema(type = "String", example = "helloiamprioritySelectionCopy.pdf", description = "우선 선발 증빙 서류")
    private String prioritySelectionCopy;

    // 흡연 여부
    @Schema(type = "Boolean", example = "true", description = "흡연 여부")
    private Boolean isSmoking; // boolean?

    // 비상 연락처
    @Schema(type = "String", example = "010-1234-5678", description = "비상 연락처")
    private String emergencyContact;

    // 비상 연락처와의 관계
    @Schema(type = "String", example = "친구", description = "부")
    private String emergencyRelation;

    // 은행명
    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    // 계좌번호
    @Schema(type = "String", example = "00000000000", description = "계좌 번호")
    private String accountNumber;
}
