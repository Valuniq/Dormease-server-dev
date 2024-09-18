package dormease.dormeasedev.domain.users.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class AdminUserInfoRes {

    @Schema(type = "String", example = "명지대학교 인문캠퍼스", description = "학교 이름")
    private String schoolName;

    @Schema(type = "String", example = "phonil", description = "로그인 아이디")
    private String loginId;

    @Schema(type = "String", example = "김도미", description = "관리자 이름")
    private String adminName;
}
