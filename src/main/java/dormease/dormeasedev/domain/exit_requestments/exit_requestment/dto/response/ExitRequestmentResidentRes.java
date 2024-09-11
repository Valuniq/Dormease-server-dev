package dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response;

import dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain.SecurityDepositReturnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ExitRequestmentResidentRes {

    // TODO : id, 사생 이름, 학번, 건물(인실 포함), 호실, 퇴실 날짜, 열쇠 수령 여부, 제출 날짜, 보증금 환급 여부(지급, 미지급)

    @Schema(type = "Long", example = "1", description = "퇴사 신청 ID")
    private Long exitRequestmentId;

    @Schema(type = "String", example = "홍길동", description = "이름")
    private String residentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
    private String dormitoryName;

    // 인실
    @Schema(type = "String", example = "4인실", description = "인실")
    private Integer roomSize;

    // 호실
    @Schema(type = "String", example = "999호", description = "호실")
    private Integer roomNumber;

    // 퇴실 날짜
    @Schema(type = "String", example = "2024-05-02", description = "퇴실 날짜")
    private LocalDate exitDate;

    // 열쇠 수령 여부
    @Schema(type = "Boolean", example = "true", description = "열쇠 수령 여부")
    private Boolean hasKey;

    // 제출 날짜 (신청 날짜)
    @Schema(type = "String", example = "2024-05-01", description = "제출 날짜 (신청 날짜)")
    private LocalDate createDate;
    
    // 보증금 환급 여부
    @Schema(type = "SecurityDepositReturnStatus", example = "UNPAID", description = "보증금 환급 여부. PAYMENT(지급), UNPAID(미지급), UNALBE(지급 불가) 中 1.")
    private SecurityDepositReturnStatus securityDepositReturnStatus;
}
