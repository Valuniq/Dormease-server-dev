package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FloorAndRoomNumberRes {

    @Schema(type = "Integer", example = "1", description= "건물의 층수입니다.")
    private Integer floor;

    @Schema(type = "Integer", example = "101", description= "호실의 시작 번호입니다.")
    private Integer startRoomNumber;

    @Schema(type = "Integer", example = "199", description= "호실의 끝 번호입니다.")
    private Integer endRoomNumber;

    @Schema(type = "Boolean", example = "true", description= "`복제`버튼의 활성화 여부입니다.")
    private Boolean isCopyButtonEnabled;

    @Builder
    public FloorAndRoomNumberRes(Integer floor, Integer startRoomNumber, Integer endRoomNumber, boolean isCopyButtonEnabled) {
        this.floor = floor;
        this.startRoomNumber = startRoomNumber;
        this.endRoomNumber = endRoomNumber;
        this.isCopyButtonEnabled = isCopyButtonEnabled;
    }
}
