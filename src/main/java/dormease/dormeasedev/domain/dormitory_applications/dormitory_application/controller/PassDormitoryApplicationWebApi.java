package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationDormitoryRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassAllDormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassDormitoryApplicationRes;
import dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response.ExitRequestmentResidentRes;
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

@Tag(name = "[WEB] Pass Dormitory Application Web API", description = "WEB에서 사용할 합격자 명단 프로세스 API입니다.")
public interface PassDormitoryApplicationWebApi {

    @Operation(summary = "전체 합격자 목록 조회 API", description = "전체 합격자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "전체 합격자 목록 조회 성공 - PassDormitoryApplicationRes 구성",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassDormitoryApplicationRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "200", description = "전체 합격자 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PassAllDormitoryApplicationRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "전체 합격자 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findAllPassDormitoryApplications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "전체 합격자 목록 검색 API", description = "전체 합격자 목록을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "전체 합격자 목록 검색 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassDormitoryApplicationRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "전체 합격자 목록 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/search/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> searchAllPassDormitoryApplications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "입사 신청 설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam(value = "searchWord") String searchWord
    );

    @Operation(summary = "입사 신청 설정의 기숙사 목록 조회 API", description = "입사 신청 설정의 기숙사 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "입사 신청 설정의 기숙사 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationDormitoryRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "입사 신청 설정의 기숙사 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/dormitories/{dormitoryApplicationSettingId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findDormitoriesByDormitoryApplicationSetting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "입사 신청 설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId
    );

    @Operation(summary = "기숙사 별 합격자 목록 조회 API", description = "기숙사 별 합격자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "기숙사 별 합격자 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassDormitoryApplicationRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "기숙사 별 합격자 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/{dormitoryApplicationSettingId}/{dormitoryId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findPassDormitoryApplicationsByDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "입사 신청 설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "기숙사 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryId") Long dormitoryId
    );

    @Operation(summary = "기숙사 별 합격자 목록 검색 API", description = "기숙사 별 합격자 목록을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "기숙사 별 합격자 목록 검색 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassDormitoryApplicationRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "기숙사 별 합격자 목록 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/search/{dormitoryApplicationSettingId}/{dormitoryId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> searchPassDormitoryApplicationsByDormitory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "입사 신청 설정 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam(value = "searchWord") String searchWord,
            @Parameter(description = "기숙사 ID를 입력해주세요.", required = true) @PathVariable(name = "dormitoryId") Long dormitoryId
    );

}
