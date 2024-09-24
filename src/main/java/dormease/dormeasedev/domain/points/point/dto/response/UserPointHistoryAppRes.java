package dormease.dormeasedev.domain.points.point.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserPointHistoryAppRes {

    @Schema(type = "Long", example = "1", description= "userPoint 테이블의 id입니다,")
    private Long userPointId;

    @Schema(type = "String", example = "화재대피훈련 참여", description= "상벌점 내역의 내용입니다.")
    private String content;

    @Schema(type = "LocalDate", example = "2024-04-26", description = "상벌점이 부여된 날짜입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createdDate;

    @Schema(type = "Integer", example = "1", description= "상벌점 내역의 점수입니다.")
    private Integer score;

    @Builder
    public UserPointHistoryAppRes(Long userPointId, String content, LocalDate createdDate, Integer score) {
        this.userPointId = userPointId;
        this.content = content;
        this.score = score;
        this.createdDate = createdDate;
    }
}
