package dormease.dormeasedev.domain.users.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ModifyAdminNameReq {

    @Schema(type = "String", example = "김도미", description = "관리자 이름")
    @NotBlank
    private String adminName;
}
