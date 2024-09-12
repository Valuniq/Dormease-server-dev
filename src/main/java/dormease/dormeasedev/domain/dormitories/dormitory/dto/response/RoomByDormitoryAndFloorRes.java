package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomByDormitoryAndFloorRes {

    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long id;

    @Schema(type = "Integer", example = "101", description= "호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "String", example = "MALE/FEMALE", description= "성별입니다.")
    private String gender;

    @Schema(type = "Integer", example = "1/2/3/4/5/6", description= "인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "0", description= "호실의 현재 인원 수를 보여줍니다.")
    private Integer currentPeople;

    @Builder
    public RoomByDormitoryAndFloorRes(Long id, Integer roomNumber, Integer roomSize, String gender, Integer currentPeople) {
        this.id= id;
        this.roomNumber = roomNumber;
        this.roomSize = roomSize;
        this.gender = gender;
        this.currentPeople = currentPeople;
    }
}