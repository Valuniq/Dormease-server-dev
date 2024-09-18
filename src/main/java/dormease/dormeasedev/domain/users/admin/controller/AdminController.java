package dormease.dormeasedev.domain.users.admin.controller;

import dormease.dormeasedev.domain.users.admin.dto.request.CheckSecurityCodeReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminNameReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminPasswordReq;
import dormease.dormeasedev.domain.users.admin.service.AdminService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/admin/account")
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse> findAdminUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(adminService.findAdminUserInfo(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PutMapping("/name")
    public ResponseEntity<Void> modifyAdminUserName(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody ModifyAdminNameReq modifyAdminNameReq
    ) {
        adminService.modifyAdminUserName(userDetailsImpl, modifyAdminNameReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/password")
    public ResponseEntity<Void> modifyAdminUserPassword(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody ModifyAdminPasswordReq modifyAdminPasswordReq
    ) {
        adminService.modifyAdminUserPassword(userDetailsImpl, modifyAdminPasswordReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/securityCode")
    public ResponseEntity<ApiResponse> checkSecurityCode(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody CheckSecurityCodeReq checkSecurityCodeReq
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(adminService.checkSecurityCode(userDetailsImpl, checkSecurityCodeReq))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}


