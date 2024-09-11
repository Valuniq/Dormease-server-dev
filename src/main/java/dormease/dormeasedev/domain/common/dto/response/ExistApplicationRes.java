package dormease.dormeasedev.domain.common.dto.response;

import dormease.dormeasedev.domain.users.user.domain.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ExistApplicationRes {

    @Schema(type = "Boolean", example = "true", description = "입사 신청 여부입니다. true : 이미 입사 신청한 상태")
    private Boolean existDormitoryApplication;

    @Schema(type = "UserType", example = "BLACKLIST", description = "유저 타입입니다. USER / ADMIN / BLACKLIST / RESIDENT 中 1")
    private UserType userType;

    @Schema(type = "Boolean", example = "true", description = "룸메이트 그룹 참여 여부입니다. true : 이미 참여한 상태")
    private Boolean existRoommateTempApplication;

    @Schema(type = "Boolean", example = "true", description = "룸메이트 그룹의 방장 여부입니다. 그룹 생성(참여) 후  방장 여부 확인을 위한 컬럼입니다. true : 그룹의 방장")
    private Boolean isMaster;

    @Schema(type = "Boolean", example = "true", description = "룸메이트 신청 여부입니다. true : 이미 신청한 상태")
    private Boolean existRoommateApplication;

    @Schema(type = "Boolean", example = "true", description = "퇴사 신청 여부입니다. true : 이미 퇴사 신청한 상태")
    private Boolean existExitRequestment;

    @Schema(type = "Boolean", example = "false", description = "방 배정 여부입니다. false : 방이 배정되지 않은 상태")
    private Boolean existRoom;
}
