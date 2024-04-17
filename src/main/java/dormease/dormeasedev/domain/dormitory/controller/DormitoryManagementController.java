package dormease.dormeasedev.domain.dormitory.controller;

import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryManagementDetailRes;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryManagementRes;
import dormease.dormeasedev.domain.dormitory.dto.response.RoomByDormitoryAndFloorRes;
import dormease.dormeasedev.domain.dormitory.service.DormitoryManagementService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Dormitory Management API", description = "건물 관리 페이지 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitory/management")
public class DormitoryManagementController {

    private final DormitoryManagementService dormitoryManagementService;

    @Operation(summary = "메모 저장", description = "건물별로 메모를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PutMapping("/{dormitoryId}/memo")
    public ResponseEntity<?> registerDormitoryMemo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 DormitoryMemoReq을 참고해주세요.", required = true) @Valid @RequestBody DormitoryMemoReq dormitoryMemoReq) {
        return dormitoryManagementService.registerDormitoryMemo(customUserDetails, dormitoryId, dormitoryMemoReq);
    }

    @Operation(summary = "건물명(인실) 목록 조회", description = "건물 관리 프로세스 중 건물 목록을 인실과 함께 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryManagementRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getDormitoriesByRoomSize(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dormitoryManagementService.getDormitoriesByRoomSize(customUserDetails);
    }

    @Operation(summary = "층 수 목록 조회", description = "건물 관리 프로세스 중 건물별 층 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/floor")
    public ResponseEntity<?> getFloorsByDormitory(
        @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId
        ) {
        return dormitoryManagementService.getFloorsByDormitory(customUserDetails, dormitoryId);
    }

    @Operation(summary = "호실 목록 조회", description = "건물 관리 프로세스 중 건물, 층별 호실 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RoomByDormitoryAndFloorRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/{floor}")
    public ResponseEntity<?> getFloorsByDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "floor를 입력해주세요.", required = true) @PathVariable Integer floor

    ) {
        return dormitoryManagementService.getRoomsByDormitory(customUserDetails, dormitoryId, floor);
    }

    @Operation(summary = "건물 정보 조회", description = "건물 관리 프로세스 중 건물의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryManagementDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryId}")
    public ResponseEntity<?> getDormitoryInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId
            ) {
        return dormitoryManagementService.getDormitoryInfo(customUserDetails, dormitoryId);
    }
}


