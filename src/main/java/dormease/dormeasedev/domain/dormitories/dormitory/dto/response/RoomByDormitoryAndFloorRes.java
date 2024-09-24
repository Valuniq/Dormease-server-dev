package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;


import dormease.dormeasedev.domain.users.user.domain.Gender;
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

    @Schema(type = "String", example = "MALE", description= "성별입니다. MALE 또는 FEMALE만 입력 가능합니다.")
    private Gender gender;

    @Schema(type = "Integer", example = "1", description= "인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "0", description= "호실의 현재 인원 수를 보여줍니다.")
    private Integer currentPeople;

    @Builder
    public RoomByDormitoryAndFloorRes(Long id, Integer roomNumber, Integer roomSize, Gender gender, Integer currentPeople) {
        this.id= id;
        this.roomNumber = roomNumber;
        this.roomSize = roomSize;
        this.gender = gender;
        this.currentPeople = currentPeople;
    }
}
