package dormease.dormeasedev.domain.dormitories.dormitory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotOrAssignedResidentRes {

    @Schema(type = "Long", example = "1", description= "사생의 고유 id입니다.")
    private Long id;

    @Schema(type = "String", example = "60132566", description= "사생의 학번입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "홍길동", description= "사생의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "010-0000-0000", description= "사생의 휴대전화 번호입니다.")
    private String phoneNumber;

    @Schema(type = "boolean", example = "true/false", description= "사생의 해당 호실 거주 여부입니다.")
    private boolean isAssigned;

    @Builder
    public NotOrAssignedResidentRes(Long id, String studentNumber, String name, String phoneNumber, boolean isAssigned) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAssigned = isAssigned;
    }
}
