package dormease.dormeasedev.domain.blacklist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BlackListContentReq {

    @Schema(type = "Long", example = "1", description = "블랙리스트 ID")
    private Long blacklistId;

    @NotBlank
    @Schema(type = "String", example = "생활관 내 흡연", description= "블랙리스트 사유입니다.")
    @Size(max = 30, message = "사유는 최대 30글자까지 입력 가능합니다.")
    private String content;
}
