package dormease.dormeasedev.domain.dormitory.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRoomByDormitoryAndFloorRes {

    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long id;

    @Schema(type = "Integer", example = "101", description= "호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "String", example = "MALE/FEMALE", description= "성별입니다.")
    private String gender;

    @Schema(type = "Integer", example = "1/2/3/4/5/6", description= "인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "0", description= "호실의 현재 인원 수/인실을 보여줍니다.")
    private String currentPeople;

    @Builder
    public GetRoomByDormitoryAndFloorRes(Long id, Integer roomNumber, Integer roomSize, String gender, String currentPeople) {
        this.id= id;
        this.roomNumber = roomNumber;
        this.roomSize = roomSize;
        this.gender = gender;
        this.currentPeople = currentPeople;
    }
}
