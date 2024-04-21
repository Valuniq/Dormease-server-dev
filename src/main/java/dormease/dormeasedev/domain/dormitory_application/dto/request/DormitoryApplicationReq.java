package dormease.dormeasedev.domain.dormitory_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DormitoryApplicationReq {

    @Schema(type = "Long", example = "1", description = "학교 ID")
    private Long schoolId;

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;



}
