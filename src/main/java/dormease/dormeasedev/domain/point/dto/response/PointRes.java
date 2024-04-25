package dormease.dormeasedev.domain.point.dto.response;

import dormease.dormeasedev.domain.point.domain.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRes {

    private Long id;

    private String content;

    private Integer score;

    private PointType pointType;

    @Builder
    public PointRes(Long id, String content, Integer score, PointType pointType) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.pointType = pointType;
    }
}
