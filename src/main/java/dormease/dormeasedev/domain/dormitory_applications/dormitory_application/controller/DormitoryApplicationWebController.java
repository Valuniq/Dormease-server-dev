package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.controller;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ApplicationIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ModifyApplicationResultIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationWebRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DormitoryApplicationWebService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/search/{dormitoryApplicationSettingId}")
    public ResponseEntity<ApiResponse> searchDormitoryApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @RequestParam(value = "searchWord") String searchWord
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationWebService.searchDormitoryApplications(userDetailsImpl, dormitoryApplicationSettingId, searchWord))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{dormitoryApplicationSettingId}")
    public ResponseEntity<ApiResponse> findDormitoryApplicationsById(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId
    ) {
        List<DormitoryApplicationWebRes> dormitoryApplicationWebResList =
                dormitoryApplicationWebService.findDormitoryApplicationsById(userDetailsImpl, dormitoryApplicationSettingId);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationWebResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PostMapping("/inspection/{dormitoryApplicationSettingId}")
    public ResponseEntity<ApiResponse> inspectApplication(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @RequestBody ApplicationIdsReq applicationIdsReq
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationWebService.inspectApplication(userDetailsImpl, dormitoryApplicationSettingId, applicationIdsReq))
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    @Override
    @PatchMapping("/result")
    public ResponseEntity<Void> modifyApplicationResult(@RequestBody ModifyApplicationResultIdsReq modifyApplicationResultIdsReq) {
        dormitoryApplicationWebService.modifyApplicationResult(modifyApplicationResultIdsReq);
        return ResponseEntity.noContent().build();
    }
}
