package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.PassDormitoryApplicationWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Override
    @GetMapping("/search/{dormitoryApplicationSettingId}")
    public ResponseEntity<ApiResponse> searchPassDormitoryApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @RequestParam(value = "searchWord") String searchWord
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(passDormitoryApplicationWebService.searchPassDormitoryApplications(userDetailsImpl, dormitoryApplicationSettingId, searchWord))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
