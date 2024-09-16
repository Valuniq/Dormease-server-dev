package dormease.dormeasedev.domain.dormitories.dormitory.controller;

import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AddRoomNumberReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.RoomSettingReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.UpdateDormitoryNameReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.DormitorySettingDetailRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.DormitorySettingListRes;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.RoomSettingRes;
import dormease.dormeasedev.domain.dormitories.dormitory.service.DormitorySettingDetailService;
import dormease.dormeasedev.domain.dormitories.dormitory.service.DormitorySettingService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.exception.ExceptionResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Dormitory Setting API", description = "건물 설정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitory/setting")
public class DormitorySettingWebController {

    private final DormitorySettingService dormitorySettingService;
    private final DormitorySettingDetailService dormitorySettingDetailService;

    @Operation(summary = "건물 추가", description = "건물 설정 프로세스 중 건물을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping("")
    public ResponseEntity<?> registerDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return dormitorySettingService.registerDormitory(customUserDetails);
    }

    @Operation(summary = "건물 목록 조회", description = "건물 설정 프로세스 중 건물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitorySettingListRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getDormitories(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dormitorySettingService.getDormitoriesBySchool(customUserDetails);
    }

    @Operation(summary = "건물 상세 조회", description = "건물 설정 프로세스 중 건물을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitorySettingDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/image")
    public ResponseEntity<?> updateDormitoryImage(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "업로드할 이미지 파일 (Multipart form-data 형식)") @RequestPart MultipartFile image) throws IOException {
        return dormitorySettingService.updateDormitoryImage(customUserDetails, dormitoryId, image);
    }

    @Operation(summary = "건물 삭제", description = "건물 설정 프로세스 중 건물을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/{dormitoryId}/name")
    public ResponseEntity<?> updateDormitoryName(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 UpdateDormitoryNameReq을 참고해주세요.", required = true) @Valid @RequestBody UpdateDormitoryNameReq updateDormitoryNameReq) {
        return dormitorySettingService.updateDormitoryName(customUserDetails, dormitoryId, updateDormitoryNameReq);
    }

    @Operation(summary = "호실 생성", description = "건물 설정 프로세스 중 특정 기숙사의 층 수를 추가해 호실을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping("/{dormitoryId}/room")
    public ResponseEntity<?> registerDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 AddRoomNumberReq을 참고해주세요.", required = true) @Valid @RequestBody AddRoomNumberReq addRoomNumberReq) {
        return dormitorySettingDetailService.addFloorAndRoomNumber(customUserDetails, dormitoryId, addRoomNumberReq);
    }

    @Operation(summary = "호실 정보 추가", description = "건물 세부 설정 프로세스 중 필터를 이용하여 호실 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping("/room/setting")
    public ResponseEntity<?> updateRoomSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 RoomSettingReq을 참고해주세요.", required = true) @RequestBody List<RoomSettingReq> roomSettingReqs) {
        // 입사신청기간에는 수정 불가 - 추후 구현
        return dormitorySettingDetailService.updateRoomSetting(customUserDetails, roomSettingReqs);
    }

    @Operation(summary = "호실 조회", description = "건물 세부 설정 프로세스 중 건물별, 층별로 호실을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoomSettingRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/{floor}/room")
    public ResponseEntity<?> getRoomSettingsByDormitoryAndFloor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "건물의 층 수를 입력해주세요.", required = true) @PathVariable Integer floor
            ) {
        return dormitorySettingDetailService.getRoomsByDormitoryAndFloor(customUserDetails, dormitoryId, floor);
    }

    @Operation(summary = "호실 삭제", description = "건물 세부 설정 프로세스 중 특정 층의 호실을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{dormitoryId}/{floor}/room")
    public ResponseEntity<?> deleteRoomsByFloor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "건물의 층 수를 입력해주세요.", required = true) @PathVariable Integer floor) {
        return dormitorySettingDetailService.deleteRoomsByFloor(customUserDetails, dormitoryId, floor);
    }
}
