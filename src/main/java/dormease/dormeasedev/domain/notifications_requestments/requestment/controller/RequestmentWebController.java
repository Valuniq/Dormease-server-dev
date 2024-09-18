package dormease.dormeasedev.domain.notifications_requestments.requestment.controller;

import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request.ModifyProgressionReq;
import dormease.dormeasedev.domain.notifications_requestments.requestment.service.RequestmentWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/requestments")
public class RequestmentWebController implements RequestmentWebApi {

    private final RequestmentWebService requestmentWebService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse> findRequestments(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(requestmentWebService.findRequestments(userDetailsImpl, page - 1))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{requestmentId}")
    public ResponseEntity<ApiResponse> findRequestment(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable("requestmentId") Long requestmentId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(requestmentWebService.findRequestment(userDetailsImpl, requestmentId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PutMapping("/{requestmentId}")
    public ResponseEntity<?> modifyRequestmentProgression(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable("requestmentId") Long requestmentId,
            @RequestBody ModifyProgressionReq modifyProgressionReq
    ) {
        requestmentWebService.modifyRequestmentProgression(userDetailsImpl, requestmentId, modifyProgressionReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{requestmentId}")
    public ResponseEntity<?> deleteRequestment(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable("requestmentId") Long requestmentId
    ) {
        requestmentWebService.deleteRequestment(userDetailsImpl, requestmentId);
        return ResponseEntity.noContent().build();
    }
}
