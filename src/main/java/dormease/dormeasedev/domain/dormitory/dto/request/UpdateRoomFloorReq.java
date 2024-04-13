package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRoomFloorReq {

    @NotNull
    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer floor;

    @NotNull
    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer newFloor;
}
