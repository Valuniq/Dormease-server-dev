package dormease.dormeasedev.domain.common.controller;

import dormease.dormeasedev.domain.common.dto.response.ExistApplicationRes;
import dormease.dormeasedev.domain.common.service.CommonService;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.exception.ExceptionResponse;
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

@Tag(name = "Common API", description = "APP - 플로우를 위한 공용 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/common")
public class CommonController {

    private final CommonService commonService;

    // Description : 입사 / 룸메이트 / 퇴사 신청 여부
    @Operation(summary = "입사 / 룸메이트 / 퇴사 신청 여부 반환" , description = "입사 / 룸메이트 / 퇴사 신청 여부를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 / 룸메이트 / 퇴사 신청 여부 반환 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExistApplicationRes.class))}),
            @ApiResponse(responseCode = "400", description = "입사 / 룸메이트 / 퇴사 신청 여부 반환 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> checkApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return commonService.checkApplication(customUserDetails);
    }


}
