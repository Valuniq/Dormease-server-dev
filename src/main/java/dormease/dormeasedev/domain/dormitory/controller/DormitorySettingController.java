package dormease.dormeasedev.domain.dormitory.controller;

import dormease.dormeasedev.domain.dormitory.dto.request.*;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryRes;
import dormease.dormeasedev.domain.dormitory.dto.response.GetDormitoryDetailRes;
import dormease.dormeasedev.domain.dormitory.dto.response.RoomSettingRes;
import dormease.dormeasedev.domain.dormitory.service.DormitorySettingDetailService;
import dormease.dormeasedev.domain.dormitory.service.DormitorySettingService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Dormitory Setting API", description = "건물 설정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitory/setting")
public class DormitorySettingController {

    private final DormitorySettingService dormitorySettingService;
    private final DormitorySettingDetailService dormitorySettingDetailService;

    @Operation(summary = "건물 추가", description = "건물 설정 프로세스 중 건물을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("")
    public ResponseEntity<?> registerDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 RegisterDormitoryReq을 참고해주세요.", required = true) @Valid @RequestPart RegisterDormitoryReq registerDormitoryReq,
            @Parameter(description = "업로드할 이미지 파일 (Multipart form-data 형식)") @RequestPart MultipartFile image) {
        return dormitorySettingService.registerDormitory(customUserDetails, registerDormitoryReq, image);
    }

    @Operation(summary = "건물 목록 조회", description = "건물 설정 프로세스 중 건물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getDormitories(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dormitorySettingService.getDormitoriesExceptGenderBySchool(customUserDetails);
    }

    @Operation(summary = "건물 상세 조회", description = "건물 설정 프로세스 중 건물을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetDormitoryDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryId}")
    public ResponseEntity<?> getDormitoryDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId) {
        return dormitorySettingDetailService.getDormitoryDetails(customUserDetails, dormitoryId);
    }

    @Operation(summary = "건물 사진 변경", description = "건물 설정 프로세스 중 건물을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/image")
    public ResponseEntity<?> updateDormitoryImage(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "업로드할 이미지 파일 (Multipart form-data 형식)") @RequestPart MultipartFile image) {
        return dormitorySettingService.updateDormitoryImage(customUserDetails, dormitoryId, image);
    }

    @Operation(summary = "건물 삭제", description = "건물 설정 프로세스 중 건물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{dormitoryId}")
    public ResponseEntity<?> deleteDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId) {
        return dormitorySettingService.deleteDormitory(customUserDetails, dormitoryId);
    }

    @Operation(summary = "건물명 수정", description = "건물명을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/name")
    public ResponseEntity<?> updateDormitoryName(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 UpdateDormitoryNameReq을 참고해주세요.", required = true) @Valid @RequestBody UpdateDormitoryNameReq updateDormitoryNameReq) {
        return dormitorySettingService.updateDormitoryName(customUserDetails, dormitoryId, updateDormitoryNameReq);
    }

    @Operation(summary = "층 수, 호실 수정", description = "건물 세부 설정 프로세스 중 특정 기숙사의 층 수와 각 층별 호실 개수를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PatchMapping("/{dormitoryId}/floor-room")
    public ResponseEntity<?> updateFloor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 UpdateRoomNumberAndFloorReq을 참고해주세요.", required = true) @Valid @RequestBody UpdateRoomNumberAndFloorReq updateRoomNumberAndFloorReq) {
        return dormitorySettingDetailService.updateRoomNumberAndFloor(customUserDetails, dormitoryId, updateRoomNumberAndFloorReq);
    }

    @Operation(summary = "층 수, 호실 추가", description = "건물 설정 프로세스 중 특정 기숙사의 층 수와 각 층별 호실을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/floor-room")
    public ResponseEntity<?> registerDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 AddRoomNumberReq을 참고해주세요.", required = true) @Valid @RequestBody AddRoomNumberReq addRoomNumberReq) {
        return dormitorySettingDetailService.addFloorAndRoomNumber(customUserDetails, dormitoryId, addRoomNumberReq);
    }

    @Operation(summary = "호실 정보 추가", description = "건물 세부 설정 프로세스 중 필터를 이용하여 호실 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/room/setting")
    public ResponseEntity<?> updateRoomSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 RoomSettingReq을 참고해주세요.", required = true) @RequestBody List<RoomSettingReq> roomSettingReqs) {
        // 입사신청기간에는 수정 불가 - 추후 구현
        return dormitorySettingDetailService.updateRoomSetting(customUserDetails, roomSettingReqs);
    }

    @Operation(summary = "호실 조회", description = "건물 세부 설정 프로세스 중 건물별, 층별로 호실을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RoomSettingRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/{floor}/room")
    public ResponseEntity<?> getRoomSettingsByDormitoryAndFloor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "건물의 층 수를 입력해주세요.", required = true) @PathVariable Integer floor) {
        return dormitorySettingDetailService.getRoomsByDormitoryAndFloor(customUserDetails, dormitoryId, floor);
    }

    @Operation(summary = "호실 삭제", description = "건물 세부 설정 프로세스 중 특정 층의 호실을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{dormitoryId}/{floor}/room")
    public ResponseEntity<?> deleteRoomsByFloor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "건물의 층 수를 입력해주세요.", required = true) @PathVariable Integer floor) {
        return dormitorySettingDetailService.deleteRoomsByFloor(customUserDetails, dormitoryId, floor);
    }
}
