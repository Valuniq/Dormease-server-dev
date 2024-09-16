package dormease.dormeasedev.domain.points.point.controller;

import dormease.dormeasedev.domain.points.point.dto.request.AddPointToResidentReq;
import dormease.dormeasedev.domain.points.point.dto.request.DeleteUserPointReq;
import dormease.dormeasedev.domain.points.point.dto.request.PointListReq;
import dormease.dormeasedev.domain.points.point.dto.response.PointRes;
import dormease.dormeasedev.domain.points.point.dto.response.ResidentInfoRes;
import dormease.dormeasedev.domain.points.point.dto.response.TotalUserPointRes;
import dormease.dormeasedev.domain.points.point.service.PointService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Point Management API", description = "상/벌점 관리 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/points")
public class PointWebController {

    private final PointService pointService;

    @Operation(summary = "상벌점 내역 조회", description = "상벌점 관리 프로세스 중 관리자가 등록한 상벌점 내역을 목록으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PointRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> registerPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 PointListReq을 참고해주세요.", required = true) @Valid @RequestBody PointListReq pointListReq
            ) {
        return pointService.registerPointList(customUserDetails, pointListReq);
    }

    @Operation(summary = "상벌점 내역 삭제", description = "상벌점 관리 프로세스 중 관리자가 등록한 상벌점 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping("/{residentId}")
    public ResponseEntity<?> addPointsToResident(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "Schemas의 AddPointToResidentReq을 참고해주세요.", required = true) @Valid @RequestBody List<AddPointToResidentReq> addPointToResidentReqList
    ) {
        return pointService.addUserPoints(customUserDetails, residentId, addPointToResidentReqList);
    }

    @Operation(summary = "상벌점 내역 조회", description = "상벌점 관리 프로세스 중 사생의 상벌점 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TotalUserPointRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{residentId}")
    public ResponseEntity<?> getResidentsPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "상벌점 내역을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return pointService.getUserPoints(customUserDetails, residentId, page - 1);
    }

    @Operation(summary = "상벌점 내역 삭제", description = "상벌점 관리 프로세스 중 사생의 상벌점 내역을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> deleteResidentsPoints(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "Schemas의 DeleteUserPointReq을 참고해주세요.", required = true) @Valid @RequestBody List<DeleteUserPointReq> deleteUserPointReqs
    ) {
        return pointService.deleteUserPoints(customUserDetails, residentId, deleteUserPointReqs);
    }

    @Operation(summary = "사생 목록 조회 및 정렬", description = "상벌점 관리 프로세스 중 사생을 조회 또는 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "bonusPoint, minusPoint 중 정렬 기준을 입력해주세요. 미입력 시 기본 정렬은 이름 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 오름차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return pointService.getResidents(customUserDetails, sortBy, isAscending, page - 1);
    }

    @Operation(summary = "사생 검색 및 정렬", description = "상벌점 관리 프로세스 중 사생을 학번 또는 이름으로 검색 또는 정렬하여 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchResidentsByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "bonusPoint, minusPoint 중 정렬 기준을 입력해주세요. 미입력 시 정렬은 이름 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 오름차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return pointService.getSearchResidents(customUserDetails, keyword, sortBy, isAscending, page - 1);
    }
}
