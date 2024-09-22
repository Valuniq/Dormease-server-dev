package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationDetailRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationSimpleRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DormitoryApplicationAppService;
import dormease.dormeasedev.global.common.Message;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dormitory Appliation API", description = "입사 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/dormitoryApplication")
public class DormitoryApplicationAppController {

    private final DormitoryApplicationAppService dormitoryApplicationAppService;

    // Description : 입사 신청
    @Operation(summary = "입사 신청", description = "입사 신청을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "입사 신청(생성) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청(생성) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> dormitoryApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "Schemas의 DormitoryApplicationReq를 참고해주세요.", required = true) @Valid @RequestBody DormitoryApplicationReq dormitoryApplicationReq
    ) {
        return dormitoryApplicationAppService.dormitoryApplication(userDetailsImpl, dormitoryApplicationReq);
    }

    // Description : 입사 신청 내역 조회 (현재)
    @Operation(summary = "입사 신청 내역 조회", description = "입사 신청 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 내역 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryApplicationDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 내역 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping
    public ResponseEntity<?> findMyDormitoryApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationAppService.findMyDormitoryApplication(userDetailsImpl);
    }

    // Description : 이전 입사 신청 내역 목록 조회 (여기서 내역은 history. 이번 입사 신청 내역 제외)
    @Operation(summary = "이전 입사 신청 내역 목록 조회", description = "이전 입사 신청 내역 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이전 입사 신청 내역 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DormitoryApplicationSimpleRes.class)))}),
            @ApiResponse(responseCode = "400", description = "이전 입사 신청 내역 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/history")
    public ResponseEntity<?> findMyDormitoryApplicationHistory(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationAppService.findMyDormitoryApplicationHistory(userDetailsImpl);
    }

    // Description : 입사 신청 상세 조회
    @Operation(summary = "입사 신청 상세 조회", description = "입사 신청을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입사 신청 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DormitoryApplicationDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "입사 신청 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/{dormitoryApplicationId}")
    public ResponseEntity<?> findDormitoryApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "dormitory id를 입력해주세요.", required = true) @PathVariable Long dormitoryApplicationId
    ) {
        return dormitoryApplicationAppService.findDormitoryApplication(userDetailsImpl, dormitoryApplicationId);
    }

    // Description : 이동 합격 수락
    @Operation(summary = "이동 합격 수락", description = "이동 합격을 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이동 합격 수락 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이동 합격 수락 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping("/accept")
    public ResponseEntity<?> acceptMovePass(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationAppService.acceptMovePass(userDetailsImpl);
    }

    // Description : 이동 합격 거절
    @Operation(summary = "이동 합격 거절", description = "이동 합격을 거절합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이동 합격 거절 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이동 합격 거절 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping("/reject")
    public ResponseEntity<?> rejectMovePass(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return dormitoryApplicationAppService.rejectMovePass(userDetailsImpl);
    }


}
