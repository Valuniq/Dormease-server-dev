package dormease.dormeasedev.domain.points.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteUserPointReq {

    @Schema(type = "Long", example = "1", description= "userPoint 테이블의 id입니다.")
    private Long userPointId;
}
