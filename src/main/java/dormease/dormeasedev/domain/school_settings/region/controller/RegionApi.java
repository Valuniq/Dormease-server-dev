package dormease.dormeasedev.domain.school_settings.region.controller;

import dormease.dormeasedev.domain.school_settings.region.dto.RegionRes;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "[WEB] Region API", description = "기준 설정 프로세스 中 지역 관련 API입니다.")
public interface RegionApi {

    @Operation(summary = "광역시도 지역 목록 조회 API", description = "광역시도 지역 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "광역시도 지역 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RegionRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "광역시도 지역 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findBigRegions();

    @Operation(summary = "광역시도 별 시군구 지역 목록 조회 API", description = "광역시도 별 시군구 지역 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "광역시도 별 시군구 지역 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RegionRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "광역시도 지역 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/{regionId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findSmallRegions(
            @Parameter(description = "광역시도 지역 id를 입력해주세요.", required = true) @PathVariable(value = "regionId") Long regionId
    );
}
