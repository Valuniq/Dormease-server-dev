package dormease.dormeasedev.domain.users.resident.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import dormease.dormeasedev.domain.users.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResidentInfoReq {

    @Schema(type = "String", example = "사용자", description= "사생의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "MALE", description= "사생의 성별입니다. MALE, FEMALE 중 1개")
    private Gender gender;

    @Schema(type = "Boolean", example = "true", description= "사생의 열쇠 수령 여부입니다.")
    private Boolean hasKey;

    @Schema(type = "ResidentDormitoryInfoReq", example = "Schemas의 ResidentDormitoryInfoReq 확인해주세요.", description= "변경하려는 사생의 기숙사정보입니다.")
    private ResidentDormitoryInfoReq residentDormitoryInfoReq;
}
