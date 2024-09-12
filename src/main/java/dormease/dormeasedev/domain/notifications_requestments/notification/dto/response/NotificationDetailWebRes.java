package dormease.dormeasedev.domain.notifications_requestments.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.notifications_requestments.file.dto.response.FileRes;
import dormease.dormeasedev.domain.notifications_requestments.image.dto.response.ImageRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotificationDetailWebRes {

    // TODO : 최상단 고정 여부, 제목, 작성자, 작성일, 수정일, 내용, 첨부파일
    @Schema(type = "Boolean", example = "true", description = "상단핀 고정 체크 여부")
    private Boolean pinned;

    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항(FAQ) 제목")
    private String title;

    @Schema(type = "String", example = "홍길동", description = "작성자 이름")
    private String writer;

    @Schema(type = "LocalDate", example = "2024-05-01", description = "작성일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;

    @Schema(type = "LocalDate", example = "2024-05-10", description = "수정일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate modifiedDate;

    @Schema(type = "String", example = "html로 변환된 내용", description = "공지사항(FAQ) 내용")
    private String content;

    @Schema(type = "ImageRes", description = "공지사항(FAQ) 텍스트 에디터 속 이미지 목록")
    private List<ImageRes> imageResList = new ArrayList<>();

    @Schema(type = "ImageRes", description = "공지사항(FAQ) 첨부파일 목록")
    private List<FileRes> fileList = new ArrayList<>();

}
