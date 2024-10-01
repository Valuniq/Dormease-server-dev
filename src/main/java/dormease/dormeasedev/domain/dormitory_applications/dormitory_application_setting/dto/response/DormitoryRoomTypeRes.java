package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryRoomTypeRes {

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitoryRoomTypeId;

    @Schema(type = "String", example = "4동", description = "기숙사명")
    private String dormitoryName;

    @Schema(type = "Integer", example = "4", description = "인실")
    private Integer roomSize;

    @Schema(type = "Gender", example = "MALE", description = "성별입니다. MALE, FEMALE, EMPTY 中 1")
    private Gender gender;

    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer dormitorySize; // 수용 인원
}
