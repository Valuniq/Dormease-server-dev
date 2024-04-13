package dormease.dormeasedev.domain.dormitory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoomSettingReq {

    @NotNull
    @Schema(type = "Long", example = "1", description= "호실의 고유 id입니다.")
    private Long id;

    @NotBlank
    @Schema(type = "String", example = "MALE/FEMALE", description= "성별입니다.")
    private String gender;

    @NotNull
    @Schema(type = "Integer", example = "1/2/3/4/5/6", description= "인실입니다.")
    private Integer roomSize;

    @NotNull
    @Schema(type = "Boolean", example = "true/false", description= "열쇠 수령 여부입니다.")
    private Boolean hasKey;

    @Schema(type = "Boolean", example = "true/false", description= "활성화 여부입니다.")
    private Boolean isActivated;
}
