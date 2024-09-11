package dormease.dormeasedev.domain.notifications_requestments.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ImageUrlRes {

    @Schema(type = "String", example = "www.dasdsadsa", description = "텍스트 에디터 속 이미지 경로")
    private String imageUrl;
}
