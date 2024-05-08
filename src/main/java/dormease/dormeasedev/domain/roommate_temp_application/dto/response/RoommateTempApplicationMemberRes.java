package dormease.dormeasedev.domain.roommate_temp_application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RoommateTempApplicationMemberRes {

    @Schema(type = "Long", example = "1", description = "사생 ID")
    private Long residentId;

    @Schema(type = "String", example = "홍길동", description = "사생 이름")
    private String name;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "Boolean", example = "true", description = "방장 여부를 반환합니다. ex) true - 방장")
    private Boolean isMaster;

    @Schema(type = "String", example = "12345678", description = "그룹 코드입니다.")
    private String code;

    @Schema(type = "Boolean", example = "true", description = "신청 여부를 반환합니다.")
    private Boolean isApplied;
}
