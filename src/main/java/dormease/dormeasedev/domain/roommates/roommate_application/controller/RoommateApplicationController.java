package dormease.dormeasedev.domain.roommates.roommate_application.controller;

import dormease.dormeasedev.domain.roommates.roommate_application.service.RoommateApplicationService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Roommate Temp Application API", description = "APP - 룸메이트 임시 신청 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app/roommateApplication")
public class RoommateApplicationController implements RoommateApplicationApi {

    private final RoommateApplicationService roommateApplicationService;

    @Override
    @PutMapping
    public ResponseEntity<Void> applyRoommate(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        roommateApplicationService.applyRoommate(userDetailsImpl);
        return ResponseEntity.noContent().build();
    }
}
