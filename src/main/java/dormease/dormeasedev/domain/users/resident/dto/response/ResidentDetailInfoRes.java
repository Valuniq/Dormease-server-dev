package dormease.dormeasedev.domain.users.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResidentDetailInfoRes {

    @Schema(type = "ResidentPrivateInfoRes", example = "ResidentPrivateInfoRes를 확인해주세요.", description= "사생의 개인정보입니다.")
    public ResidentPrivateInfoRes residentPrivateInfoRes;

    @Schema(type = "ResidentDormitoryInfoRes", example = "ResidentDormitoryInfoRes를 확인해주세요.", description= "사생의 기숙사 정보입니다.")
    public ResidentDormitoryInfoRes residentDormitoryInfoRes;

    @Builder
    public ResidentDetailInfoRes(ResidentPrivateInfoRes residentPrivateInfoRes, ResidentDormitoryInfoRes residentDormitoryInfoRes) {
        this.residentPrivateInfoRes = residentPrivateInfoRes;
        this.residentDormitoryInfoRes = residentDormitoryInfoRes;
    }
}
