package dormease.dormeasedev.domain.roommate_temp_application.controller;

import dormease.dormeasedev.domain.roommate_temp_application.service.RoommateTempApplicationService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Roommate Temp Application API", description = "APP - 룸메이트 임시 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/roommateTempApplication")
public class RoommateTempApplicationController {

    private final RoommateTempApplicationService roommateTempApplicationService;

    // Description : 그룹 생성 (코드 발급) (방장 o)
    @Operation(summary = "그룹 생성 (코드 발급)", description = "룸메이트 그룹을 생성합니다. (코드 발급)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "그룹 생성 (코드 발급) 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "그룹 생성 (코드 발급) 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return roommateTempApplicationService.createRoommateTempApplication(customUserDetails);
    }

    // Description : 그룹 삭제 (방장 o)
    @Operation(summary = "그룹 삭제", description = "룸메이트 그룹을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "그룹 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping
    public ResponseEntity<?> deleteRoommateTempApplication(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return roommateTempApplicationService.deleteRoommateTempApplication(customUserDetails);
    }

    // Description : 룸메이트 신청 (방장 o)



    // Description : 코드 입력 (방장 x)

    // Description : 그룹 나가기 (방장 x)

}
