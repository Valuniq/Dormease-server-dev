package dormease.dormeasedev.domain.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TotalUserPointRes {

    @Schema(type = "UserPointDetailRes", description= "UserPointDetailRes의 List형식이며, 특정 회원의 상벌점 내역입니다.")
    private List<UserPointDetailRes> userPointDetailRes;

    @Schema(type = "Integer", example = "1", description= "특정 회원의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "특정 회원의 벌점 총점입니다.")
    private Integer minusPoint;

    @Builder
    public TotalUserPointRes(List<UserPointDetailRes> userPointDetailRes, Integer bonusPoint, Integer minusPoint) {
        this.userPointDetailRes = userPointDetailRes;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
    }
}
