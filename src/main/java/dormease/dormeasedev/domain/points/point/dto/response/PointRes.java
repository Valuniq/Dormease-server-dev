package dormease.dormeasedev.domain.points.point.dto.response;

import dormease.dormeasedev.domain.points.point.domain.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRes {

    @Schema(type = "Long", example = "1", description= "상점 또는 벌점의 id입니다.")
    private Long pointId;

    @Schema(type = "String", example = "화재대피훈련 참여", description= "상벌점 내역의 내용입니다.")
    private String content;

    @Schema(type = "Integer", example = "1~99", description= "상벌점 내역의 점수입니다.")
    private Integer score;

    @Schema(type = "String", example = "BONUS", description= "상벌점 내역의 점수입니다. BONUS/MINUS")
    private PointType pointType;

    @Builder
    public PointRes(Long pointId, String content, Integer score, PointType pointType) {
        this.pointId = pointId;
        this.content = content;
        this.score = score;
        this.pointType = pointType;
    }
}
