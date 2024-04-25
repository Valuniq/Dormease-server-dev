package dormease.dormeasedev.domain.point.controller;

import dormease.dormeasedev.domain.point.dto.response.PointRes;
import dormease.dormeasedev.domain.point.service.PointService;
import dormease.dormeasedev.domain.user.dto.response.ActiveUserInfoRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point Management API", description = "상/벌점 관리 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/points")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "사생 목록 조회", description = "상벌점 관리 프로세스 중 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PointRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "0") Integer page

    ) {
        return pointService.getResidents(customUserDetails, page);
    }

    @Operation(summary = "사생 정렬", description = "상벌점 관리 프로세스 중 사생 목록을 입력받은 기준으로 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PointRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/sort")
    public ResponseEntity<?> sortedResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "bonusPoint, minusPoint 중 정렬 기준을 입력해주세요.", required = true) @RequestParam String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요.", required = true) @RequestParam Boolean isAscending,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "0") Integer page

    ) {
        return pointService.getSortedResidents(customUserDetails, sortBy, isAscending, page);
    }

    @Operation(summary = "사생 검색", description = "상벌점 관리 프로세스 중 사생을 학번 또는 이름으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PointRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchResidentsByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return pointService.getSearchResidents(customUserDetails, keyword, page);
    }
}
