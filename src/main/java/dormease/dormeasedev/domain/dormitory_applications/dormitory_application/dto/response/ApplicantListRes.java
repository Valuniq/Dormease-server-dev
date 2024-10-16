package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApplicantListRes {

    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID")
    private Long dormitoryApplicationSettingId;

    @Schema(type = "List<DormitoryApplicationWebRes>", description = "입사 신청 목록입니다. (신청자 명단)")
    private List<DormitoryApplicationWebRes> dormitoryApplicationWebResList;
}
