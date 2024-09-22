package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.PassDormitoryApplicationRes;
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

@Tag(name = "[WEB] Pass Dormitory Application Web API", description = "WEB에서 사용할 합격자 명단 프로세스 API입니다.")
public interface PassDormitoryApplicationWebApi {

    @Operation(summary = "합격자 목록 조회 API", description = "합격자 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "합격자 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PassDormitoryApplicationRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "합격자 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findPassDormitoryApplications(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );
}
