package dormease.dormeasedev.domain.roommate_temp_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ExistRoommateTempApplicationRes {

    @Schema(type = "Boolean", example = "true", description = "이미 소속된 그룹이 있는지 반환합니다. ex) true - 소속된 그룹 존재")
    private Boolean existRoommateTempApplication;

    @Schema(type = "Boolean", example = "true", description = "방장 여부를 반환합니다. ex) true - 방장")
    private Boolean isMaster;
}
