package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ModifyApplicationResultIdsReq {

    @Schema(type = "Long", example = "1", description = "입사 신청 설정 ID를 입력해주세요.")
    private Long dormitoryApplicationSettingId;
    
    @Schema(type = "List<ApplicationResultIdsReq>", description= "합격자 검사 결과 별로 입사 신청 ID를 입력해주세요.")
    private List<ApplicationResultIdsReq> applicationResultIdsReqList;
}
