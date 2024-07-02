package dormease.dormeasedev.domain.refund_requestment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class RefundRequestmentReq {

    @Schema(type = "String", example = "2024-05-02", description = "퇴실 날짜")
    private LocalDate exitDate;

    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    @Schema(type = "String", example = "00000000000", description = "계좌 번호")
    private String accountNumber;
}
