package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DepositDormitoryApplicationWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/depositDormitoryApplication")
public class DepositDormitoryApplicationWebController implements DepositDormitoryApplicationWebApi {

    private final DepositDormitoryApplicationWebService depositDormitoryApplicationWebService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse> findDepositDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(depositDormitoryApplicationWebService.findDepositDormitoryApplications(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
