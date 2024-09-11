package dormease.dormeasedev.domain.users.resident.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResidentPrivateInfoReq {

    // 생활관비 납부 여부
    @Schema(type = "Boolean", example = "true", description= "사생의 생활관비 납부 여부입니다.")
    private Boolean dormitoryPayment;

    // 열쇠 수령 여부
    @Schema(type = "Boolean", example = "true", description= "사생의 열쇠 수령 여부입니다.")
    private Boolean hasKey;

    // 은행명
    @Schema(type = "String", example = "신한", description= "사생의 계좌 은행명입니다.")
    private String bankName;

    // 계좌번호
    @Schema(type = "String", example = "109568712054", description= "사생의 계좌번호입니다.")
    private String accountNumber;

    // 비상 연락처
    @Schema(type = "String", example = "010xxxxxxxx", description= "사생의 비상연락처입니다.")
    private String emergencyContact;

    // 비상 연락처와의 관계
    @Schema(type = "String", example = "부", description= "사생의 비상연락처와의 관계입니다.")
    private String emergencyRelation;
}

