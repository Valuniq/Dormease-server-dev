package dormease.dormeasedev.domain.point.dto.response;

import dormease.dormeasedev.domain.point.domain.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserPointDetailRes {

    @Schema(type = "Long", example = "1", description= "userPoint 테이블의 id입니다,")
    private Long id;

    @Schema(type = "String", example = "화재대피훈련 참여", description= "상벌점 내역의 내용입니다.")
    private String content;

    @Schema(type = "LocalDate", example = "2024-04-26", description = "상벌점이 부여된 날짜입니다.")
    private LocalDate createdAt;

    @Schema(type = "Integer", example = "1~99", description= "상벌점 내역의 점수입니다.")
    private Integer score;

    @Schema(type = "Integer", example = "BONUS/MINUS", description= "상벌점 내역의 점수입니다.")
    private PointType pointType;

    @Builder
    public UserPointDetailRes(Long id, String content, LocalDate createdAt, PointType pointType, Integer score) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.pointType = pointType;
        this.score = score;
    }
}
