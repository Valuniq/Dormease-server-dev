package dormease.dormeasedev.domain.requestment.controller;

import dormease.dormeasedev.domain.notification.dto.request.WriteNotificataionReq;
import dormease.dormeasedev.domain.refund_requestment.dto.response.RefundRequestmentRes;
import dormease.dormeasedev.domain.requestment.dto.request.WriteRequestmentReq;
import dormease.dormeasedev.domain.requestment.dto.response.RequestmentRes;
import dormease.dormeasedev.domain.requestment.service.RequestmentAppService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Requestment API", description = "APP - 요청사항 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/requestments")
public class RequestmentAppController {

    private final RequestmentAppService requestmentAppService;

    // Description : 요청사항 작성
    @Operation(summary = "요청사항 작성" , description = "요청사항을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청사항 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "요청사항 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> writeRequestment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 WriteRequestmentReq을 참고해주세요.", required = true) @RequestBody WriteRequestmentReq writeRequestmentReq
    ) {
        return requestmentAppService.writeRequestment(customUserDetails, writeRequestmentReq);
    }

    // Description : 요청사항 목록 조회
    @Operation(summary = "요청사항 목록 조회" , description = "요청사항 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "요청사항 목록 조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RequestmentRes.class)))}),
            @ApiResponse(responseCode = "200", description = "요청사항 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "요청사항 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findRequestments(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "요청사항 목록을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return requestmentAppService.findRequestments(customUserDetails, page - 1);
    }

}
