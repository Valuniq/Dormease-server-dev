package dormease.dormeasedev.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPointAppRes {

    @Schema(type = "Integer", example = "1", description= "특정 회원의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "특정 회원의 벌점 총점입니다.")
    private Integer minusPoint;

    @Builder
    public UserPointAppRes (Integer bonusPoint, Integer minusPoint) {
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
    }
}
