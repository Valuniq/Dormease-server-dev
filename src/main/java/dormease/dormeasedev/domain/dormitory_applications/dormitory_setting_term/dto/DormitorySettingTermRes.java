package dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitorySettingTermRes {

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitoryRoomTypeId;

    @Schema(type = "String", example = "3동", description = "기숙사 이름")
    private String dormitoryName;

    @Schema(type = "Integer", example = "4(인실)", description = "기숙사 인실 종류")
    private Integer roomSize;

    @Schema(type = "Gender", example = "MALE", description = "성별입니다. MALE, FEMALE, EMPTY 中 1")
    private Gender gender;

    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer acceptLimit;
}
