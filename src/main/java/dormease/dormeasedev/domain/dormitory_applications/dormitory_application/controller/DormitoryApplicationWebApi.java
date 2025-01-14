package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ApplicationIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ModifyApplicationResultIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.ApplicantListRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationWebRes;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[WEB] Dormitory Application API", description = "WEB에서 사용할 입사 신청자 명단 프로세스 API입니다.")
public interface DormitoryApplicationWebApi {

    @Operation(summary = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 API", description = "현재 입사 신청 설정(가장 최근 입사 신청 설정)에 대한 입사 신청 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "0", description = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 성공 - dataList 구성",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}),
            @ApiResponse(
                    responseCode = "200", description = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApplicantListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findDormitoryApplications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "입사 신청 설정에 대한 입사 신청 목록 검색 API", description = "입사 신청 설정에 대한 입사 신청 목록을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "입사 신청 설정에 대한 입사 신청 목록 검색 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "입사 신청 설정에 대한 입사 신청 목록 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/search/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> searchDormitoryApplications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "입사 신청 설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam(value = "searchWord") String searchWord
    );

    @Operation(summary = "입사 신청 설정(ID)으로 입사 신청 목록 조회 API", description = "입사 신청 설정(ID)으로 입사 신청 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "입사 신청 설정(ID)으로 입사 신청 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "입사 신청 설정(ID)으로 입사 신청 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findDormitoryApplicationsById(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitoryApplicationSettingId(입사 신청 설정 id)를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId
    );

    @Operation(summary = "합격자 검사 API", description = "합격자 검사를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "합격자 검사 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "합격자 검사 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PostMapping("/inspection/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> inspectApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitoryApplicationSettingId(입사 신청 설정 id)를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "신청자 명단에 포함된 입사 신청 id 목록을 입력해주세요.", required = true) @RequestBody ApplicationIdsReq applicationIdsReq
    );

    @Operation(summary = "합격자 검사 결과 저장 API", description = "합격자 검사 결과를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "합격자 검사 결과 저장 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Void.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "합격자 검사 결과 저장 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PatchMapping("/result")
    ResponseEntity<Void> modifyApplicationResult(
            @Parameter(description = "합격자 검사 결과 별로 입사 신청 ID를 입력해주세요.", required = true) @RequestBody ModifyApplicationResultIdsReq modifyApplicationResultIdsReq
    );
}
