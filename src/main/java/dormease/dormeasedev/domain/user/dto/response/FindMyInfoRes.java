package dormease.dormeasedev.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FindMyInfoRes {

    @Schema(type = "String", example = "phonil", description= "로그인 시 사용될 아이디입니다.")
    private String loginId;

    @Schema(type = "String", example = "60190927", description= "사용자의 학번입니다. (혹은 수험번호)")
    private String studentNumber;

    @Schema(type = "String", example = "01012345678", description= "사용자의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "boolean", example = "false", description= "블랙리스트 사용자 여부입니다.")
    private boolean isBlackList;
}
