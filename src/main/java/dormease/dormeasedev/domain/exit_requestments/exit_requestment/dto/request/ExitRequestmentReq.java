package dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ExitRequestmentReq {

    @Schema(type = "String", example = "2024-05-02", description = "퇴실 날짜")
    private LocalDate exitDate;

    @Schema(type = "Boolean", example = "true", description = "열쇠 수령 여부")
    private Boolean hasKey;

    @Schema(type = "String", example = "ABCDEFGHI", description = "열쇠 번호입니다. 열쇠 수령 여부가 false라면 null을 입력해주세요.")
    private String keyNumber;

    @Schema(type = "String", example = "신한은행", description = "은행명")
    private String bankName;

    @Schema(type = "String", example = "9999-99-999999", description = "계좌 번호")
    private String accountNumber;
}
