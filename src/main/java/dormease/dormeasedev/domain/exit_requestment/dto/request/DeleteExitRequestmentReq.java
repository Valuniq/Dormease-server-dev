package dormease.dormeasedev.domain.exit_requestment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteExitRequestmentReq {

    @Schema(type = " List<Long>", example = "[1, 2, 3]", description = "퇴사 신청 ID 목록")
    private List<Long> exitRequestmentIdList = new ArrayList<>();
}
