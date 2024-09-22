package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.PassDormitoryApplicationWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/passDormitoryApplication")
public class PassDormitoryApplicationWebController implements PassDormitoryApplicationWebApi {

    private final PassDormitoryApplicationWebService passDormitoryApplicationWebService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse> findPassDormitoryApplications(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(passDormitoryApplicationWebService.findPassDormitoryApplications(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
