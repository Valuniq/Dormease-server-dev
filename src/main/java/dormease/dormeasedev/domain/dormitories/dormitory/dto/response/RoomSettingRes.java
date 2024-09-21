package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomSettingRes {

    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long id;

    @Schema(type = "Integer", example = "101", description= "호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "Integer", example = "1", description= "호실이 위치한 층수입니다.")
    private Integer floor;

    @Schema(type = "String", example = "MALE", description= "성별입니다.")
    private String gender;

    @Schema(type = "Integer", example = "2", description= "인실입니다.")
    private Integer roomSize;

    @Schema(type = "Boolean", example = "false", description= "열쇠 수령 여부입니다.")
    private Boolean hasKey;

    @Schema(type = "Boolean", example = "true", description= "활성화 여부입니다.")
    private Boolean isActivated;

    @Schema(type = "Boolean", example = "true", description= "해당 호실의 사생 배정 여부 입니다.")
    private Boolean hasResident;


    @Builder
    public RoomSettingRes(Long id, Integer floor, Integer roomNumber, String gender, Integer roomSize, Boolean hasKey, Boolean isActivated, Boolean hasResident) {
        this.id = id;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.gender = gender;
        this.roomSize = roomSize;
        this.hasKey = hasKey;
        this.isActivated = isActivated;
        this.hasResident = hasResident;
    }
}
