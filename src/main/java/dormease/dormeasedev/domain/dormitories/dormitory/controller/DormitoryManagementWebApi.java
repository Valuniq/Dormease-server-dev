package dormease.dormeasedev.domain.dormitories.dormitory.controller;

import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.response.*;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[WEB] 건물 관리 프로세스 API", description = "건물 관리 프로세스에 해당되는 API입니다.")
public interface DormitoryManagementWebApi {

    @Operation(summary = "메모 저장", description = "건물별로 메모를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "저장 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = void.class))}),
            @ApiResponse(
                    responseCode = "400", description = "저장 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/{dormitoryId}/memo")
    ResponseEntity<?> registerDormitoryMemo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "Schemas의 DormitoryMemoReq을 참고해주세요.", required = true) @Valid @RequestBody DormitoryMemoReq dormitoryMemoReq
    );

    @Operation(summary = "건물명 목록 조회", description = "건물 관리 프로세스 중 건물 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryManagementListRes.class)))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping()
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getDormitoriesByRoomSize(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "층 수 목록 조회", description = "건물 관리 프로세스 중 건물별 층 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FloorByDormitoryRes.class)))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/floor")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getFloorsByDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId
    );

    @Operation(summary = "호실 목록 조회", description = "건물 관리 프로세스 중 건물, 층별 호실 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoomByDormitoryAndFloorRes.class)))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryId}/{floor}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getFloorsByDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId,
            @Parameter(description = "층 수를 입력해주세요.", required = true) @PathVariable Integer floor
    );

    @Operation(summary = "건물 정보 조회", description = "건물 관리 프로세스 중 건물의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryManagementDetailRes.class))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getDormitoryInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryId
    );

    @Operation(summary = "미배정 사생 조회", description = "수기 방배정 시 호실 미배정 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotOrAssignedResidentRes.class))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/rooms/{roomId}/not-assigned")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getNotAssignedResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "room id를 입력해주세요.", required = true) @PathVariable Long roomId
    );

    @Operation(summary = "특정 호실에 배정된 사생 조회", description = "건물 관리 프로세스 중 특정 호실에 배정된 사생 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NotOrAssignedResidentRes.class))}),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/rooms/{roomId}/assigned")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getAssignedResidents(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long roomId
    );

    @Operation(summary = "수기 방배정", description = "건물 관리 프로세스 중 미배정 사생을 대상으로 호실을 배정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "배정 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = void.class))}),
            @ApiResponse(
                    responseCode = "400", description = "배정 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/rooms/{roomId}/manual")
    ResponseEntity<?> assignedResidentsToRooms(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "room id를 입력해주세요.", required = true) @PathVariable Long roomId,
            @Parameter(description = "Schemas의 AssignedResidentToRoomReq을 참고해주세요.", required = true) @Valid @RequestBody AssignedResidentToRoomReq assignedResidentToRoomReq
    );
}
