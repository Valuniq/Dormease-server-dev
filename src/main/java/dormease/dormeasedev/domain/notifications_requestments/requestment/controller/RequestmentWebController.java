package dormease.dormeasedev.domain.notifications_requestments.requestment.controller;

import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request.ModifyProgressionReq;
import dormease.dormeasedev.domain.notifications_requestments.requestment.service.RequestmentWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.CustomUserDetails;
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
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(requestmentWebService.findRequestments(customUserDetails, page - 1))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{requestmentId}")
    public ResponseEntity<ApiResponse> findRequestment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("requestmentId") Long requestmentId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(requestmentWebService.findRequestment(customUserDetails, requestmentId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PutMapping("/{requestmentId}")
    public ResponseEntity<?> modifyRequestmentProgression(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("requestmentId") Long requestmentId,
            @RequestBody ModifyProgressionReq modifyProgressionReq
    ) {
        requestmentWebService.modifyRequestmentProgression(customUserDetails, requestmentId, modifyProgressionReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{requestmentId}")
    public ResponseEntity<?> deleteRequestment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("requestmentId") Long requestmentId
    ) {
        requestmentWebService.deleteRequestment(customUserDetails, requestmentId);
        return ResponseEntity.noContent().build();
    }
}
