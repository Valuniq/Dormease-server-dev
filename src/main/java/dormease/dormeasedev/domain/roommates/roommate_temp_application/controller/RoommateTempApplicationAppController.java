package dormease.dormeasedev.domain.roommates.roommate_temp_application.controller;

import dormease.dormeasedev.domain.roommates.roommate_temp_application.dto.response.ExistRoommateTempApplicationRes;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.dto.response.RoommateTempApplicationMemberRes;
import dormease.dormeasedev.domain.roommates.roommate_temp_application.service.RoommateTempApplicationService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Roommate Temp Application API", description = "APP - 룸메이트 임시 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/roommateTempApplication")
public class RoommateTempApplicationAppController {

    private final RoommateTempApplicationService roommateTempApplicationService;

    // Description : 룸메이트 임시 신청 여부 + 방장 여부 조회
    @Operation(summary = "룸메이트 임시 신청(그룹 생성) 여부 + 방장 여부 조회", description = "룸메이트 임시 신청 여부 + 방장 여부를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "룸메이트 임시 신청 여부 + 방장 여부 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExistRoommateTempApplicationRes.class))}),
            @ApiResponse(responseCode = "400", description = "룸메이트 임시 신청 여부 + 방장 여부 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/existAndMaster")
    public ResponseEntity<?> existRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return roommateTempApplicationService.existRoommateTempApplication(userDetailsImpl);
    }

    // Description : 그룹 생성 (코드 발급) (방장 o)
    @Operation(summary = "그룹 생성 (코드 발급)", description = "룸메이트 그룹을 생성합니다. (코드 발급)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "그룹 생성 (코드 발급) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "그룹 생성 (코드 발급) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return roommateTempApplicationService.createRoommateTempApplication(userDetailsImpl);
    }

    // Description : 그룹 삭제 (방장 o)
    @Operation(summary = "그룹 삭제", description = "룸메이트 그룹을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "그룹 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return roommateTempApplicationService.deleteRoommateTempApplication(userDetailsImpl);
    }

    // ---------------- 팀원 ----------------
    // Description : 코드 입력 (방장 x) : 코드 입력 후 신청하기 버튼 (그룹 참가)
    @Operation(summary = "코드 입력 (그룹 참가)", description = "코드를 입력합니다. (그룹 참가)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코드 입력 (그룹 참가) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "코드 입력 (그룹 참가) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping("/{code}")
    public ResponseEntity<?> joinRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "참가할 그룹의 코드를 입력해주세요.", required = true) @PathVariable(value = "code") String code
    ) {
        return roommateTempApplicationService.joinRoommateTempApplication(userDetailsImpl, code);
    }

    // Description : 그룹 나가기 (방장 x)
    @Operation(summary = "그룹 나가기", description = "그룹에서 나갑니다. (방장 x)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 나가기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "그룹 나가기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PatchMapping("/out")
    public ResponseEntity<?> outOfRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return roommateTempApplicationService.outOfRoommateTempApplication(userDetailsImpl);
    }

    // Description : 그룹원 조회
    @Operation(summary = "그룹원 조회", description = "그룹원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹원 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoommateTempApplicationMemberRes.class)))}),
            @ApiResponse(responseCode = "400", description = "그룹원 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/members")
    public ResponseEntity<?> findRoommateTempApplicationMembers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return roommateTempApplicationService.findRoommateTempApplicationMembers(userDetailsImpl);
    }

}
