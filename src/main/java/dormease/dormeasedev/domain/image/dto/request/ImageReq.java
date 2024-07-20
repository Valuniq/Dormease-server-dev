package dormease.dormeasedev.domain.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ImageReq {

    @Schema(type = "String", example = "www.dasdsadsa", description = "텍스트 에디터 속 이미지 경로")
    private String imageUrl;
}
