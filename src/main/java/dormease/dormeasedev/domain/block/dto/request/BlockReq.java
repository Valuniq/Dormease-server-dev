package dormease.dormeasedev.domain.block.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BlockReq {

    // TODO : 이미지 url, 블럭 순서, 블럭 내용
    //  이미지 url은 논의 필요
    @Schema(type = "String", example = "www.example.com", description = "첨부할 이미지 url. 없으면 null")
    private String imageUrl;

    @Schema(type = "Integer", example = "1", description = "블럭 순서. 작성 내용이 많아질 경우 순서대로 보여주기 위함입니다.")
    private Integer sequence;

    @Schema(type = "String", example = "내용", description = "블럭 내용. 너무 길어질 경우 새로운 블럭으로 순서에 맞게 작성할 수 있습니다.")
    private String content;
}
