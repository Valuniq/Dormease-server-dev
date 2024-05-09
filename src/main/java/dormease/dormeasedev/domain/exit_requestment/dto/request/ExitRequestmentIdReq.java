package dormease.dormeasedev.domain.exit_requestment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExitRequestmentIdReq {

    @Schema(type = "Long", example = "1", description = "퇴사 신청 ID")
    private Long exitRequestmentId;
}
