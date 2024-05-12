package dormease.dormeasedev.domain.file.dto.response;

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

    @Schema(type = "String", example = "abcdef.txt", description = "파일 경로")
    private String fileUrl;
}
