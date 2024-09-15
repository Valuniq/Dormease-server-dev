package dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response;

import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.Progression;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RequestmentDetailUserRes {

    @Schema(type = "Long", example = "1", description = "요청사항 ID")
    private Long requestmentId;

    // TODO : 본인이 작성한 요창사항인지
    @Schema(type = "Boolean", example = "true", description = "본인이 작성한 요청사항인지")
    private Boolean myRequestment;

    @Schema(type = "String", example = "제목", description = "요청사항 제목")
    private String title;

    @Schema(type = "String", example = "내용", description = "요청사항 내용")
    private String content;

    @Schema(type = "String", example = "홍길동", description = "요청사항 작성자")
    private String writer;

    @Schema(type = "LocalDate", example = "2024-05-02", description = "작성 날짜")
    private LocalDate createdDate;

    @Schema(type = "Boolean", example = "true", description = "부재 중 방문 동의 여부")
    private Boolean consentDuringAbsence;

    @Schema(type = "Boolean", example = "true", description = "공개 여부")
    private Boolean visibility;

    @Schema(type = "Progression", example = "IN_REVIEW", description = "관리자 답변 상태. IN_REVIEW(검토 중), IN_PROGRESS(진행 중), ANSWER_COMPLETED(답변 완료) 中 1")
    private Progression progression;
}
