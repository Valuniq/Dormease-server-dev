package dormease.dormeasedev.domain.exit_requestment.controller;

import dormease.dormeasedev.domain.exit_requestment.dto.response.ExitRequestmentRes;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ResidentInfoForExitRes;
import dormease.dormeasedev.domain.exit_requestment.service.ExitRequestmentAppService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ExitRequestment API", description = "APP - 퇴사 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/exitRequestment")
public class ExitRequestmentAppController {

    private final ExitRequestmentAppService exitRequestmentAppService;


    // Description : 퇴사 확인서를 위한 사생 정보 조회
    @Operation(summary = "퇴사 확인서를 위한 사생 정보 조회", description = "퇴사 확인서를 위한 사생 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퇴사 확인서를 위한 사생 정보 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResidentInfoForExitRes.class))}),
            @ApiResponse(responseCode = "400", description = "퇴사 확인서를 위한 사생 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findInfoForExitRequestment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return exitRequestmentAppService.findInfoForExitRequestment(customUserDetails);
    }
}
