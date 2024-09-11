package dormease.dormeasedev.domain.notifications_requestments.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FileRes {

    @Schema(type = "Long", example = "1", description = "File ID")
    private Long fileId;

    @Schema(type = "String", example = "https://ec-2-fdsafasfsdafs.com", description = "파일 경로")
    private String fileUrl;

    @Schema(type = "String", example = "abcdef.txt", description = "원본 파일 이름")
    private String originalFileName;
}
