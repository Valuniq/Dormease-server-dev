package dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request;

import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.Progression;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ModifyProgressionReq {

    @Schema(type = "Progression", example = "IN_REVIEW", description = "관리자 답변 상태. IN_REVIEW(검토 중), IN_PROGRESS(진행 중), ANSWER_COMPLETED(답변 완료) 中 1")
    private Progression progression;
}
