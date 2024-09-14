package dormease.dormeasedev.domain.notifications_requestments.requestment.controller;

import dormease.dormeasedev.domain.notifications_requestments.requestment.service.RequestmentWebService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/requestments")
public class RequestmentWebController implements RequestmentWebApi {

    private final RequestmentWebService requestmentWebService;

    @Override
    @GetMapping
    public ResponseEntity<?> findRequestments(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Integer page
    ) {
        return requestmentWebService.findRequestments(customUserDetails, page - 1);
    }
}
