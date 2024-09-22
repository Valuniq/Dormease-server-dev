package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[WEB] Dormitory Application API", description = "WEB에서 사용할 입사 신청자 명단 프로세스 API입니다.")
public interface DormitoryApplicationWebApi {

    @Operation(summary = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 API", description = "현재 입사 신청 설정(가장 최근 입사 신청 설정)에 대한 입사 신청 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "현재 입사 신청 설정에 대한 입사 신청 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}
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
            @Parameter(description = "입사신청설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam(value = "searchWord") String searchWord
    );

    @Operation(summary = "현재 입사 신청 설정(ID)으로 입사 신청 목록 조회 API", description = "현재 입사 신청 설정(ID)으로 입사 신청 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "현재 입사 신청 설정(ID)으로 입사 신청 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationWebRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "현재 입사 신청 설정(ID)으로 입사 신청 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findDormitoryApplicationsById(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitoryApplicationSettingId(입사 신청 설정 id)를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId
    );
}
