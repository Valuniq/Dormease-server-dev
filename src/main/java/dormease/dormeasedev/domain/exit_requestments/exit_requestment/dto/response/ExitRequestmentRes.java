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
public class ExitRequestmentRes {

    // TODO : id, 사생 이름, 학과, 학번, 학년, 휴대전화, 건물(인실), 보증금 환급 여부 (지급 불가??), 호실, 침대 번호, 열쇠 수령 여부, 열쇠 번호, 퇴실 날짜, 은행명, 계좌 번호

    @Schema(type = "Long", example = "1", description = "퇴사 신청 ID")
    private Long exitRequestmentId;

    @Schema(type = "String", example = "홍길동", description = "이름")
    private String residentName;

    @Schema(type = "String", example = "컴퓨터공학과", description = "학과")
    private String major;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "Integer", example = "1", description = "학년")
    private Integer schoolYear;

    @Schema(type = "String", example = "010-1234-5678", description= "휴대전화")
    private String phoneNumber;

    @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
    private String dormitoryName;

    @Schema(type = "String", example = "4인실", description = "인실")
    private Integer roomSize;

    @Schema(type = "SecurityDepositReturnStatus", example = "UNPAID", description = "보증금 환급 여부. PAYMENT(지급), UNPAID(미지급), UNALBE(지급 불가) 中 1.")
    private SecurityDepositReturnStatus securityDepositReturnStatus;

    @Schema(type = "String", example = "999호", description = "호실")
    private Integer roomNumber;

    @Schema(type = "String", example = "1", description = "침대 번호")
    private Integer bedNumber;

    @Schema(type = "Boolean", example = "true", description = "열쇠 수령 여부")
    private Boolean hasKey;

    @Schema(type = "String", example = "ABCDEFGHI", description = "열쇠 번호")
    private String keyNumber;

    @Schema(type = "String", example = "2024-05-02", description = "퇴실 날짜")
    private LocalDate exitDate;

    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    @Schema(type = "String", example = "9999-99-999999", description = "계좌 번호")
    private String accountNumber;

}
