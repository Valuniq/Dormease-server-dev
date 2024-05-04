package dormease.dormeasedev.domain.dormitory_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DormitoryApplicationRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 ID")
    private Long dormitoryApplicationId;

    


}
