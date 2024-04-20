package dormease.dormeasedev.domain.dormitory.dto.request.app_dormitory_application;

import dormease.dormeasedev.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindDormitoryReq {

    @Schema(type = "Long", example = "1", description = "학교 ID")
    private Long schoolId;

    @Schema(type = "Gender", example = "MALE", description = "성별 - MALE / FEMALE / EMPTY 中 1")
    private Gender gender;

    @Schema(type = "String", example = "학기", description = "거주 기간")
    private String term;
}
