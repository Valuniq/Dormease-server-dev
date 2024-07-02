package dormease.dormeasedev.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotificationWebRes {

    // TODO : 번호(?) - 인덱스로 쓰면 어떨지, 제목, 작성자, 등록일, 첨부 파일 여부, 조회수
    @Schema(type = "Long", example = "1", description = "공지사항(FAQ) ID")
    private Long notificationId;

    @Schema(type = "Boolean", example = "true", description = "상단핀 고정 체크 여부")
    private Boolean pinned;

    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항(FAQ) 제목")
    private String title;

    @Schema(type = "String", example = "홍길동", description = "작성자 이름")
    private String writer;

    @Schema(type = "LocalDate", example = "2024-05-12", description = "작성일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;

    @Schema(type = "Boolean", example = "true", description = "첨부 파일 존재 여부")
    private Boolean existFile;
}
