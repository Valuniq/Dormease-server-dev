package dormease.dormeasedev.domain.users.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class CheckSecurityCodeRes {

    @Schema(type = "String", example = "true", description= "검증 성공 여부입니다. true이면 보안 코드 일치.")
    private boolean checked;
}
