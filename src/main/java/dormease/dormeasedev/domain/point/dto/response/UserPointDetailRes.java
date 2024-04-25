package dormease.dormeasedev.domain.point.dto.response;

import dormease.dormeasedev.domain.point.domain.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserPointDetailRes {

    private Long id;

    private String content;

    private LocalDate createdAt;

    private PointType pointType;

    private Integer score;

    @Builder
    public UserPointDetailRes(Long id, String content, LocalDate createdAt, PointType pointType, Integer score) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.pointType = pointType;
        this.score = score;
    }
}
