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
    public ResponseEntity<ApiResponse> findAllPassDormitoryApplications(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(passDormitoryApplicationWebService.findPassDormitoryApplications(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
//    @GetMapping("/search/{dormitoryApplicationSettingId}")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchAllPassDormitoryApplications(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
//            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @RequestParam(value = "searchWord") String searchWord
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
//                .information(passDormitoryApplicationWebService.searchPassDormitoryApplications(userDetailsImpl, dormitoryApplicationSettingId, searchWord))
                .information(passDormitoryApplicationWebService.searchPassDormitoryApplications(userDetailsImpl, searchWord))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
//    @GetMapping("/dormitories/{dormitoryApplicationSettingId}")
    @GetMapping("/dormitories")
    public ResponseEntity<ApiResponse> findDormitoriesByDormitoryApplicationSetting(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
//            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
//                .information(passDormitoryApplicationWebService.findDormitoriesByDormitoryApplicationSetting(userDetailsImpl, dormitoryApplicationSettingId))
                .information(passDormitoryApplicationWebService.findDormitoriesByDormitoryApplicationSetting(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    @Override
//    @GetMapping("/{dormitoryApplicationSettingId}/{dormitoryId}")
    @GetMapping("/{dormitoryId}")
    public ResponseEntity<ApiResponse> findPassDormitoryApplicationsByDormitory(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
//            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @PathVariable(name = "dormitoryId") Long dormitoryId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
//                .information(passDormitoryApplicationWebService.findPassDormitoryApplicationsByDormitory(userDetailsImpl, dormitoryApplicationSettingId, dormitoryId))
                .information(passDormitoryApplicationWebService.findPassDormitoryApplicationsByDormitory(userDetailsImpl, dormitoryId))
                .build();
         return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/search/{dormitoryId}")
    public ResponseEntity<ApiResponse> searchPassDormitoryApplicationsByDormitory(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
//            @PathVariable(name = "dormitoryApplicationSettingId") Long dormitoryApplicationSettingId,
            @RequestParam(value = "searchWord") String searchWord,
            @PathVariable(name = "dormitoryId")Long dormitoryId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
//                .information(passDormitoryApplicationWebService.searchPassDormitoryApplicationsByDormitory(userDetailsImpl, dormitoryApplicationSettingId, searchWord, dormitoryId))
                .information(passDormitoryApplicationWebService.searchPassDormitoryApplicationsByDormitory(userDetailsImpl, searchWord, dormitoryId))
                .build();
        return ResponseEntity.ok(apiResponse);

    }
}
