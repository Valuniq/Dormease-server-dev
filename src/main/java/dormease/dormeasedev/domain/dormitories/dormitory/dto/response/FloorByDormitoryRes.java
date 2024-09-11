package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FloorByDormitoryRes {

    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다.")
    private Integer floor;

    @Builder
    public FloorByDormitoryRes(Integer floor) {
        this.floor = floor;
    }
}
