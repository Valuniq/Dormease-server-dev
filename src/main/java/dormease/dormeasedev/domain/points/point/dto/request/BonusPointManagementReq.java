package dormease.dormeasedev.domain.points.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class BonusPointManagementReq {

//    @NotBlank
//    @Schema(type = "Long", example = "1", description= "상점의 id입니다.")
//    private Long pointId;

    @NotBlank
    @Size(max = 30, message = "최대 30자까지 입력 가능합니다.")
    @Schema(type = "String", example = "화재대피훈련 참여", description= "상점 내역의 내용입니다.")
    private String content;

    @NotNull
    @Schema(type = "Integer", example = "1~99", description= "상점 내역의 점수입니다.")
    @Min(value = 1, message = "점수는 최소 1이상이어야 합니다.")
    @Max(value = 99, message = "점수는 최대 2자리 수까지 가능합니다.")
    private Integer score;
}
