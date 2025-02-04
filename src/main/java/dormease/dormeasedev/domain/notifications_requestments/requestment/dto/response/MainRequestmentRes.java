package dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MainRequestmentRes {

    // TODO : id, 제목, 작성일, 작성자, 관리자 답변 상태
    @Schema(type = "Long", example = "1", description = "요청사항 ID")
    private Long requestmentId;

    @Schema(type = "String", example = "제목", description = "요청사항 제목")
    private String title;

    @Schema(type = "String", example = "홍길동", description = "요청사항 작성자")
    private String writer;

    @Schema(type = "LocalDate", example = "2024-05-02", description = "작성 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;
}
