package dormease.dormeasedev.domain.school_settings.standard_setting.controller;

import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.service.StandardSettingService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
