package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PassAllDormitoryApplicationRes {

//    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID")
//    private Long dormitoryApplicationSettingId;

    @Schema(type = "PassDormitoryApplicationRes", description = "입금 완료 / 합격자 목록입니다.")
    List<PassDormitoryApplicationRes> passDormitoryApplicationResList;
}
