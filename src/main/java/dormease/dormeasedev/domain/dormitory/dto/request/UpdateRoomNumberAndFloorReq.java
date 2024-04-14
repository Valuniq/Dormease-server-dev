package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateRoomNumberAndFloorReq {

    @NotNull
    @Schema(type = "Integer", example = "1", description= "특정 건물의 기존 층수입니다. 1이상이어야 합니다.")
    @Min(value = 1, message = "층 수는 최소 1이상이어야 합니다.")
    private Integer floor;

    @Schema(type = "Integer", example = "1", description= "특정 건물의 변경된 층수입니다. 변경사항이 있는 경우에만 전달해주세요.")
    private Integer newFloor;

    @Schema(type = "Integer", example = "1~99", description= "호실의 변경된 시작 번호입니다.")
    private Integer startRoomNumber;

    @Schema(type = "Integer", example = "1~99", description= "호실의 변경된 끝 번호입니다.")
    private Integer endRoomNumber;
}
