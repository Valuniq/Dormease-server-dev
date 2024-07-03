package dormease.dormeasedev.domain.dormitory.controller;

import dormease.dormeasedev.domain.dormitory.dto.response.app_dormitory_application.FindDormitoryRes;
import dormease.dormeasedev.domain.dormitory.service.DormitoryService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dormitory API", description = "기숙사 건물 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/dormitory")
public class DormitoryAppController {

    private final DormitoryService dormitoryService;

    @Operation(summary = "건물 목록 조회", description = "입사 신청 단계 중 학교, 거주 기간, 성별 별 건물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FindDormitoryRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{termId}")
    public ResponseEntity<?> findDormitories(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "거주 기간을 입력해주세요.", required = true) @PathVariable Long termId
        ) {
        return dormitoryService.findDormitories(customUserDetails, termId);
    }

}
