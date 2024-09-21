package dormease.dormeasedev.domain.school_settings.standard_setting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class DistanceScoreReq {

    @Schema(type = "Long", example = "1", description = "거리 점수 ID")
    private Long distanceScoreId;

//    @Schema(type = "List<Long>", example = "[1, 3, 5, 7]", description = "지역 ID 리스트")
//    private List<Long> regionIdList;

    @Schema(type = "List<String>", example = "[\"서울특별시\", \"서울특별시 서대문구\", \"전라남도\"]", description = "지역 ID 리스트")
    private List<String> regionNameList;
}
