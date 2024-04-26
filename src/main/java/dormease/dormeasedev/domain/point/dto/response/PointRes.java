package dormease.dormeasedev.domain.point.dto.response;

import dormease.dormeasedev.domain.point.domain.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRes {

    @Schema(type = "Long", example = "1", description= "상점 또는 벌점의 id입니다.")
    private Long id;

    @Schema(type = "String", example = "화재대피훈련 참여", description= "상벌점 내역의 내용입니다.")
    private String content;

    @Schema(type = "Integer", example = "1~99", description= "상벌점 내역의 점수입니다.")
    private Integer score;

    @Schema(type = "Integer", example = "BONUS/MINUS", description= "상벌점 내역의 점수입니다.")
    private PointType pointType;

    @Builder
    public PointRes(Long id, String content, Integer score, PointType pointType) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.pointType = pointType;
    }
}
