package dormease.dormeasedev.domain.users.resident.controller;

import dormease.dormeasedev.domain.users.resident.dto.request.ResidentPrivateInfoReq;
import dormease.dormeasedev.domain.users.resident.dto.response.AvailableDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.users.resident.dto.response.DormitoryResidentAssignmentRes;
import dormease.dormeasedev.domain.users.resident.dto.response.ResidentDetailInfoRes;
import dormease.dormeasedev.domain.users.resident.dto.response.ResidentRes;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
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
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Tag(name = "Resident Management API", description = "사생 관리 관련 API입니다.")
public interface ResidentWebApi {

    @Operation(summary = "사생 목록 조회 및 정렬", description = "사생 관리 프로세스 중 전체 사생을 조회 또는 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("")
    ResponseEntity<?> getResidents(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "bonusPoint, minusPoint, dormitory, gender중 정렬 기준을 입력해주세요. 미입력 시 기본 정렬은 이름 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 오름차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    );

    @Operation(summary = "사생 검색 및 정렬", description = "사생 관리 프로세스 중 사생을 학번 또는 이름으로 검색 또는 정렬하여 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResidentRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/search")
    ResponseEntity<?> searchResidentsByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "bonusPoint, minusPoint, dormitory, gender 중 정렬 기준을 입력해주세요. 미입력 시 정렬은 이름 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 오름차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Parameter(description = "사생을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    );

    @Operation(summary = "사생 상세 조회", description = "사생 관리 프로세스 중 특정 사생을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResidentDetailInfoRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{residentId}")
    ResponseEntity<?> getResidentInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId
    );

    @Operation(summary = "사생 정보 수정(개인정보)", description = "사생 관리 프로세스 중 특정 사생의 개인정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/{residentId}")
    ResponseEntity<?> updateResidentPrivateInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "form-data 형식의 Multipart-file을 입력해주세요. 등본 파일입니다.") @RequestPart Optional<MultipartFile> copy,
            @Parameter(description = "form-data 형식의 Multipart-file을 입력해주세요. 우선선발증빙서류 파일입니다.") @RequestPart Optional<MultipartFile> prioritySelectionCopy,
            @Parameter(description = "ResidentPrivateInfoReq Schema를 확인해주세요", required = true) @RequestPart ResidentPrivateInfoReq residentPrivateInfoReq
    ) throws IOException;

    @Operation(summary = "사생 성별에 맞는 기숙사 조회", description = "사생의 기숙사 정보 중 성별에 맞는 기숙사 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryResidentAssignmentRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{residentId}/dormitory/{termId}")
    ResponseEntity<?> getDormitoriesAssignResident(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
            @Parameter(description = "거주기간의 id를 입력해주세요.", required = true) @PathVariable Long termId
    );

    @Operation(summary = "입사신청 및 거주기간 목록 조회", description = "사생 직접 추가 시 입사신청 및 거주기간 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AvailableDormitoryApplicationSettingRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/dormitorySettingApplication")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> getAvailableSettingAndTerm(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "사생 퇴사 처리", description = "사생 관리 프로세스 중 특정 사생을 퇴사 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "처리 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{residentId}")
    ResponseEntity<?> deleteResident (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId
    );

    @Operation(summary = "사생 블랙리스트 처리", description = "사생 관리 프로세스 중 특정 사생을 블랙리스트 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "처리 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{residentId}/blackList")
    public ResponseEntity<?> addBlackList (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId
    );

}

