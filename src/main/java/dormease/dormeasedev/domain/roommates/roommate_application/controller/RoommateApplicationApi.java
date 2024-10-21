package dormease.dormeasedev.domain.roommates.roommate_application.controller;

import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "[APP] Roommate Application API", description = "룸메이트 신청 관련 API입니다.")
public interface RoommateApplicationApi {

    // Description : 룸메이트 신청 (방장 o)
    @Operation(summary = "룸메이트 신청", description = "룸메이트 신청을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "룸메이트 신청 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(responseCode = "400", description = "룸메이트 신청 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @PutMapping
    ResponseEntity<?> applyRoommate(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );
}
