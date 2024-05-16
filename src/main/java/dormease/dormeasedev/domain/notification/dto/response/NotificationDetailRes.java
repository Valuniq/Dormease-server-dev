package dormease.dormeasedev.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.block.dto.response.BlockRes;
import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.file.dto.response.FileRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotificationDetailRes {

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

    private List<BlockRes> blockResList = new ArrayList<>();

    private List<FileRes> fileList = new ArrayList<>();

}
