package dormease.dormeasedev.domain.exit_requestment.dto.request;

import dormease.dormeasedev.domain.exit_requestment.domain.SecurityDepositReturnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ModifyDepositReq {

    @Schema(type = "SecurityDepositReturnStatus", example = "UNPAID", description = "변경할 보증금 환급 상태를 입력해주세요. PAYMENT(지급), UNPAID(미지급), UNALBE(지급 불가) 中 1.")
    private SecurityDepositReturnStatus securityDepositReturnStatus;

    @Schema(type = " List<ExitRequestmentIdReq>", example = "ExitRequestmentIdReq", description = "퇴사 신청 ID 목록")
    private List<ExitRequestmentIdReq> exitRequestmentIdReqList = new ArrayList<>();
}
