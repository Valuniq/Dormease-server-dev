package dormease.dormeasedev.domain.school_settings.standard_setting.dto.response;

import dormease.dormeasedev.domain.school_settings.region.dto.RegionRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DistanceScoreRes {

    @Schema(type = "Long", example = "1", description = "거리 점수 ID")
    private Long distanceScoreId;

    @Schema(type = "double", example = "0", description = "거리 점수입니다. 0 ~ 4.5점까지 0.5점 단위로 존재합니다.")
    private double distanceScore;

    @Schema(type = "List<RegionRes>", description = "지역 이름 목록")
    private List<RegionRes> regionResList;
}
