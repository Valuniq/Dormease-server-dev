package dormease.dormeasedev.domain.school_settings.standard_setting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DistanceScoreRes {

    @Schema(type = "double", example = "0", description = "거리 점수입니다. 0 ~ 4.5점까지 0.5점 단위로 존재합니다.")
    private double distanceScore;

    @Schema(type = "List<String>", example = "[\"서울특별시\", \"서울특별시 서대문구\", \"전라남도\"]", description = "지역 이름 목록")
    private List<String> regionNameList;
}
