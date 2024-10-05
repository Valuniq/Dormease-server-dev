package dormease.dormeasedev.domain.school_settings.region.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RegionRes {

    @Schema(type = "Long", example = "1", description = "광역시도 지역 ID")
    private Long regionId;

    @Schema(type = "String", example = "서울특별시", description = "광역시도 지역명")
    private String regionName;
}
