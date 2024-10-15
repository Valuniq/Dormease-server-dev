package dormease.dormeasedev.domain.users.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvailableTermRes {

    @Schema(type = "Long", example = "1", description = "거주기간의 id입니다.")
    private Long termId;

    @Schema(type = "String", example = "학기", description = "거주기간의 이름입니다.")
    private String termName;

    @Builder
    public AvailableTermRes(Long termId, String termName) {
        this.termId = termId;
        this.termName = termName;
    }
}
