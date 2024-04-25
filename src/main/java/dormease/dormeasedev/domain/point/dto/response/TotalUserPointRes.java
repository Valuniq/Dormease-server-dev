package dormease.dormeasedev.domain.point.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TotalUserPointRes {

    private List<UserPointDetailRes> userPointDetailRes;

    private Integer bonusPoint;

    private Integer minusPoint;

    @Builder
    public TotalUserPointRes(List<UserPointDetailRes> userPointDetailRes, Integer bonusPoint, Integer minusPoint) {
        this.userPointDetailRes = userPointDetailRes;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
    }
}
