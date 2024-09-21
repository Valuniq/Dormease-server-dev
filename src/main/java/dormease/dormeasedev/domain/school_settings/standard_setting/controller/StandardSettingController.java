package dormease.dormeasedev.domain.school_settings.standard_setting.controller;

import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.response.StandardSettingRes;
import dormease.dormeasedev.domain.school_settings.standard_setting.service.StandardSettingService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/standardSetting")
public class StandardSettingController implements StandardSettingApi {

    private final StandardSettingService standardSettingService;

    @Override
    @PostMapping
    public ResponseEntity<Void> createStandardSetting(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody CreateStandardSettingReq createStandardSettingReq
    ) {
        StandardSetting standardSetting = standardSettingService.createStandardSetting(userDetailsImpl, createStandardSettingReq);
        return ResponseEntity.created(URI.create("/api/v1/web/standardSetting/" + standardSetting.getId())).build();
    }

    @Override
    @GetMapping("/{standardSettingId}")
    public ResponseEntity<ApiResponse> findStandardSetting(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable(value = "standardSettingId") Long standardSettingId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(standardSettingService.findStandardSetting(userDetailsImpl, standardSettingId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
