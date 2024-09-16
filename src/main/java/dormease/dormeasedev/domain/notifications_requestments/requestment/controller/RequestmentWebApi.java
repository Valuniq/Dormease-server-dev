package dormease.dormeasedev.domain.notifications_requestments.requestment.controller;

import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request.ModifyProgressionReq;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentDetailAdminRes;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentRes;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.exception.ExceptionResponse;
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

@Tag(name = "[WEB] Requestment API", description = "WEB에서 사용될 요청사항 관련 API입니다.")
public interface RequestmentWebApi {

    @Operation(summary = "요청사항 목록 조회 API", description = "요청사항 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "0", description = "요청사항 목록 조회 성공 - dataList 구성",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RequestmentRes.class)))}
            ),
            @ApiResponse(
                    responseCode = "200", description = "요청사항 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "요청사항 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findRequestments(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "요청사항 목록을 페이지 별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    );

    @Operation(summary = "요청사항 상세 조회 API", description = "요청사항 상세 조회를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "요청사항 상세 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RequestmentDetailAdminRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "요청사항 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @GetMapping("/{requestmentId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> findRequestment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "요청사항의 id를 입력해주세요.", required = true) @PathVariable(value = "requestmentId") Long requestmentId
    );

    @Operation(summary = "요청사항 검토 상태 변경 API", description = "요청사항 검토 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "요청사항 검토 상태 변경 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "요청사항 검토 상태 변경 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @PutMapping("/{requestmentId}")
    ResponseEntity<?> modifyRequestmentProgression(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "요청사항의 id를 입력해주세요.", required = true) @PathVariable(value = "requestmentId") Long requestmentId,
            @Parameter(description = "Schemas의 ModifyProgressionReq를 참고해주세요.", required = true) @RequestBody ModifyProgressionReq modifyProgressionReq
    );

    @Operation(summary = "요청사항 삭제 API", description = "요청사항을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "요청사항 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "요청사항 삭제 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            )
    })
    @DeleteMapping("/{requestmentId}")
    ResponseEntity<?> deleteRequestment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "요청사항의 id를 입력해주세요.", required = true) @PathVariable(value = "requestmentId") Long requestmentId
    );
}
