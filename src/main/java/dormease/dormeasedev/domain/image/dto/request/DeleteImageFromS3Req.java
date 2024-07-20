package dormease.dormeasedev.domain.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class DeleteImageFromS3Req {

    @Schema(type = "List<String>", example = "[www.dasdsadsa, www.dasgggg]", description = "텍스트 에디터 속 이미지 경로 목록")
    private List<String> imageUrlList;
}
