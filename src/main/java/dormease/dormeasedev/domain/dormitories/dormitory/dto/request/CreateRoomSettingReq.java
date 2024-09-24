package dormease.dormeasedev.domain.dormitories.dormitory.dto.request;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateRoomSettingReq {
    @NotNull
    @Schema(type = "Integer", example = "101", description = "호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "String", example = "MALE", description = "성별입니다. MALE/FEMALE로 구성될 수 있음")
    private Gender gender;

    @Schema(type = "Integer", example = "1", description = "인실입니다.")
    private Integer roomSize;

    @Schema(type = "Boolean", example = "true", description = "열쇠 수령 여부입니다. true, false로 구성될 수 있음")
    private Boolean hasKey;

    @Schema(type = "Boolean", example = "true", description = "활성화 여부입니다. true, false로 구성될 수 있음")
    private Boolean isActivated;
}
