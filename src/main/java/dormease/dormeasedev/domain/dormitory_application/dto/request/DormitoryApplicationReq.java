package dormease.dormeasedev.domain.dormitory_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DormitoryApplicationReq {

    @Schema(type = "Long", example = "1", description = "학교 ID")
    private Long schoolId;

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "Long", example = "1", description = "기숙사 - 거주기간 중간 테이블 ID")
    private Long dormitoryTermRelationId;

    @Schema(type = "Long", example = "1", description = "식권 ID")
    private Long mealTicketId;

    // 등본
    private String copy;

    // 우선 선발 증빙 서류
    private String prioritySelectionCopy;

    // 흡연 여부
    private Boolean isSmoking;

    // 비상 연락처
    private String emergencyContact;

    // 비상 연락처와의 관계
    private String emergencyRelation;

    // 은행명
    private String bankName;

    // 계좌번호
    private String accountNumber;

}
