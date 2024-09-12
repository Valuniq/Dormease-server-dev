package dormease.dormeasedev.domain.points.point.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResidentInfoRes {

    @Schema(type = "Long", example = "1", description= "사생의 id입니다.")
    private Long id;

    @Schema(type = "String", example = "사용자", description= "사생의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "사생의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "전화번호", description= "사생의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "Integer", example = "1", description= "사생의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "사생의 벌점 총점입니다.")
    private Integer minusPoint;

    @Schema(type = "String", example = "건물명(인실)", description= "사생이 거주하는 건물의 이름(+인실)입니다.")
    private String dormitory;

    @Schema(type = "Integer", example = "1", description= "사생이 거주하는 호실의 번호입니다.")
    private Integer room;

    @Builder
    public ResidentInfoRes(Long id, String name, String studentNumber, String phoneNumber, Integer bonusPoint, Integer minusPoint, String dormitory, Integer room) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.dormitory = dormitory;
        this.room = room;
    }

}
