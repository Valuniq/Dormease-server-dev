package dormease.dormeasedev.domain.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FloorAndRoomNumberRes {

    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다.")
    private Integer floor;

    @Schema(type = "Integer", example = "1~99", description= "호실의 시작 번호입니다.")
    private Integer startRoomNumber;

    @Schema(type = "Integer", example = "1~99", description= "호실의 끝 번호입니다.")
    private Integer endRoomNumber;

    @Builder
    public FloorAndRoomNumberRes(Integer floor, Integer startRoomNumber, Integer endRoomNumber) {
        this.floor = floor;
        this.startRoomNumber = startRoomNumber;
        this.endRoomNumber = endRoomNumber;
    }
}
