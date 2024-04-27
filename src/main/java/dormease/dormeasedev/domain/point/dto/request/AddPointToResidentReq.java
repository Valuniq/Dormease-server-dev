package dormease.dormeasedev.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AddPointToResidentReq {

    @Schema(type = "Long", example = "1", description= "상점 또는 벌점의 id입니다.")
    private Long pointId;
}
