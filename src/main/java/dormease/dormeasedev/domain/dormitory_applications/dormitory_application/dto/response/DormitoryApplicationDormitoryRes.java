package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DormitoryApplicationDormitoryRes {

    // 건물 id
    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "String", example = "건물명", description= "사생이 거주하는 건물명입니다.")
    private String dormitoryName;
}
