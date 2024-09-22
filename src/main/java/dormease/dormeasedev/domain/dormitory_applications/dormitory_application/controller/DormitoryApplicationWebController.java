package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DormitoryApplicationWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitoryApplication")
public class DormitoryApplicationWebController implements DormitoryApplicationWebApi {

    private final DormitoryApplicationWebService dormitoryApplicationWebService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse> findDormitoryApplications(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationWebService.findDormitoryApplications(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/search/{searchWord}")
    public ResponseEntity<ApiResponse> searchDormitoryApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "searchWord") String searchWord
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationWebService.searchDormitoryApplications(userDetailsImpl, searchWord))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
