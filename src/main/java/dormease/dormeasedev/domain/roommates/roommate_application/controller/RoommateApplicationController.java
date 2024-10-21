package dormease.dormeasedev.domain.roommates.roommate_application.controller;

import dormease.dormeasedev.domain.roommates.roommate_application.service.RoommateApplicationService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Roommate Temp Application API", description = "APP - 룸메이트 임시 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/roommateApplication")
public class RoommateApplicationController implements RoommateApplicationApi {

    private final RoommateApplicationService roommateApplicationService;

    @Override
    @PatchMapping
    public ResponseEntity<Void> applyRoommate(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        roommateApplicationService.applyRoommate(userDetailsImpl);
        return ResponseEntity.noContent().build();
    }
}
