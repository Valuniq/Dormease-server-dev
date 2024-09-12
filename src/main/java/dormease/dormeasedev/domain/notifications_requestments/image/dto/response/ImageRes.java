package dormease.dormeasedev.domain.notifications_requestments.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ImageRes {

    @Schema(type = "Long", example = "1", description = "텍스트 에디터 속 이미지 id")
    private Long imageId;

    @Schema(type = "String", example = "www.dasdsadsa", description = "텍스트 에디터 속 이미지 경로")
    private String imageUrl;
}
