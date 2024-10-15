package dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ModifyDormitorySettingTermReq {

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitorySettingTermId;

    @Schema(type = "Long", example = "1", description = "기숙사(인실/성별 구분) ID")
    private Long dormitoryRoomTypeId;

    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer acceptLimit;
}
