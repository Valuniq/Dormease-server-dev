package dormease.dormeasedev.domain.points.point.controller;

import dormease.dormeasedev.domain.points.point.dto.response.UserPointAppRes;
import dormease.dormeasedev.domain.points.point.dto.response.UserPointHistoryAppRes;
import dormease.dormeasedev.domain.points.point.service.PointService;
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

@Tag(name = "Point Management API", description = "[APP] 상/벌점 관리 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/points")
public class PointAppController {

    private final PointService pointService;

    // 상벌점 조회
    @Operation(summary = "상벌점 점수 조회", description = "마이페이지에서 사용자의 상/벌점 각각의 총합을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserPointAppRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getUserPoint(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return pointService.getUserPointTotal(customUserDetails);
    }

    // 상벌점 내역 조회
    @Operation(summary = "상벌점 내역 조회", description = "마이페이지에서 type에 따라 사용자의 상/벌점 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserPointHistoryAppRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{type}")
    public ResponseEntity<?> getUserPointHistory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "상점/벌점에 따라 BONUS, MINUS을 입력해주세요.", required = true) @PathVariable String type
    ) {
        return pointService.getUserPointHistory(customUserDetails, type);
    }

}
