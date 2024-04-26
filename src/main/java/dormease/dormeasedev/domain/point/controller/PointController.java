package dormease.dormeasedev.domain.point.controller;

import dormease.dormeasedev.domain.point.dto.request.AddPointToResidentReq;
import dormease.dormeasedev.domain.point.dto.request.DeleteUserPointReq;
import dormease.dormeasedev.domain.point.dto.request.PointListReq;
import dormease.dormeasedev.domain.point.dto.response.PointRes;
import dormease.dormeasedev.domain.point.dto.response.TotalUserPointRes;
import dormease.dormeasedev.domain.point.dto.response.ResidentInfoRes;
import dormease.dormeasedev.domain.point.service.PointService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Point Management API", description = "상/벌점 관리 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/points")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "상벌점 내역 조회", description = "상벌점 관리 프로세스 중 관리자가 등록한 상벌점 내역을 목록으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PointRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/detail")
    public ResponseEntity<?> getPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return pointService.getPointList(customUserDetails);
    }

    @Operation(summary = "상벌점 내역 등록", description = "상벌점 관리 프로세스 중 관리자가 상벌점 내역을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/detail")
    public ResponseEntity<?> registerPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 PointListReq을 참고해주세요.", required = true) @Valid @RequestBody PointListReq pointListReq
            ) {
        return pointService.registerPointList(customUserDetails, pointListReq);
    }

    @Operation(summary = "상벌점 내역 삭제", description = "상벌점 관리 프로세스 중 관리자가 등록한 상벌점 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/detail/{pointId}")
    public ResponseEntity<?> deletePoint(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "point id를 입력해주세요.", required = true) @PathVariable Long pointId
    ) {
        return pointService.deletePoint(customUserDetails, pointId);
    }

    @Operation(summary = "상벌점 부여", description = "상벌점 관리 프로세스 중 사생에게 상벌점을 부여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{residentId}")
    public ResponseEntity<?> addPointsToResident(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "상점, 벌점 중 부여하고자 하는 유형을 bonus 또는 minus로 입력해주세요.", required = true) @RequestParam String pointType,
            @Parameter(description = "Schemas의 AddPointToResidentReq을 참고해주세요.", required = true) @Valid @RequestBody List<AddPointToResidentReq> addPointToResidentReqList
    ) {
        return pointService.addUserPoints(customUserDetails, residentId, addPointToResidentReqList, pointType);
    }

    @Operation(summary = "상벌점 내역 조회", description = "상벌점 관리 프로세스 중 사생의 상벌점 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TotalUserPointRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{residentId}")
    public ResponseEntity<?> getResidentsPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "상벌점 내역을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "0") Integer page

    ) {
        return pointService.getUserPoints(customUserDetails, residentId, page);
    }

    @Operation(summary = "상벌점 내역 삭제", description = "상벌점 관리 프로세스 중 사생의 상벌점 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> deleteResidentsPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "Schemas의 DeleteUserPointReq을 참고해주세요.", required = true) @Valid @RequestBody List<DeleteUserPointReq> deleteUserPointReqs
    ) {
        return pointService.deleteUserPoints(customUserDetails, residentId, deleteUserPointReqs);
    }

    @Operation(summary = "사생 목록 조회", description = "상벌점 관리 프로세스 중 회원의 상벌점 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentInfoRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 0부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "0") Integer page

    ) {
        return pointService.getResidents(customUserDetails, page);
    }

    @Operation(summary = "사생 정렬", description = "상벌점 관리 프로세스 중 회원의 상벌점 목록을 입력받은 기준으로 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentInfoRes.class)))}),
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

    @Operation(summary = "사생 검색", description = "상벌점 관리 프로세스 중 회원을 학번 또는 이름으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentInfoRes.class)))}),
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
