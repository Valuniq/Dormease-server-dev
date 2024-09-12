package dormease.dormeasedev.domain.users.resident.dto.response;

import dormease.dormeasedev.domain.users.user.domain.Gender;
import dormease.dormeasedev.domain.users.user.domain.SchoolStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResidentRes {

    @Schema(type = "Long", example = "1", description= "사생의 id입니다.")
    private Long residentId;

    @Schema(type = "String", example = "사용자", description= "사생의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "사생의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "MALE / FEMALE", description= "사생의 성별입니다.")
    private Gender gender;

    @Schema(type = "Integer", example = "1", description= "사생의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "사생의 벌점 총점입니다.")
    private Integer minusPoint;

    @Schema(type = "String", example = "건물명", description= "사생이 거주하는 건물명입니다.")
    private String dormitoryName;

    @Schema(type = "Integer", example = "2", description= "사생이 거주하는 건물의 인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "1", description= "사생이 거주하는 호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "String", example = "재학", description= "사생의 학적 상태입니다.")
    private SchoolStatus schoolStatus;

    @Builder
    public ResidentRes(Long residentId, String name, String studentNumber, Gender gender, String dormitoryName, Integer roomSize, Integer roomNumber, Integer bonusPoint, Integer minusPoint, SchoolStatus schoolStatus) {
        this.residentId = residentId;
        this.name = name;
        this.studentNumber = studentNumber;
        this.gender = gender;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.roomNumber = roomNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.schoolStatus = schoolStatus;
    }
}
