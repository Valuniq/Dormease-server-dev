package dormease.dormeasedev.domain.users.resident.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResidentInfoReq {

    @Schema(type = "ResidentPrivateInfoReq", example = "Schemas의 ResidentPrivateInfoReq 확인해주세요.", description= "변경하려는 사생의 개인정보입니다.")
    private ResidentPrivateInfoReq residentPrivateInfoReq;

    @Schema(type = "ResidentDormitoryInfoReq", example = "Schemas의 ResidentDormitoryInfoReq 확인해주세요.", description= "변경하려는 사생의 기숙사정보입니다.")
    private ResidentDormitoryInfoReq residentDormitoryInfoReq;
}
