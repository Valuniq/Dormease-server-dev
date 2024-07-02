package dormease.dormeasedev.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.block.dto.response.BlockRes;
import dormease.dormeasedev.domain.file.dto.response.FileRes;
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
public class NotificationDetailAppRes {

    // TODO : 제목, 작성일, 내용, 첨부파일
    @Schema(type = "String", example = "2024-1 전자식권 사용방법 안내", description = "공지사항(FAQ) 제목")
    private String title;

    @Schema(type = "LocalDate", example = "2024-05-01", description = "작성일")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;

    private List<BlockRes> blockResList = new ArrayList<>();

    private List<FileRes> fileList = new ArrayList<>();
}
