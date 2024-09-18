package dormease.dormeasedev.domain.users.admin.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.admin.domain.Admin;
import dormease.dormeasedev.domain.users.admin.domain.repository.AdminRepository;
import dormease.dormeasedev.domain.users.admin.dto.request.CheckSecurityCodeReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminNameReq;
import dormease.dormeasedev.domain.users.admin.dto.request.ModifyAdminPasswordReq;
import dormease.dormeasedev.domain.users.admin.dto.response.AdminUserInfoRes;
import dormease.dormeasedev.domain.users.admin.dto.response.CheckSecurityCodeRes;
import dormease.dormeasedev.domain.users.admin.exception.InvalidSecurityCodeException;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInfoRes findAdminUserInfo(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        return AdminUserInfoRes.builder()
                .schoolName(school.getName())
                .adminName(adminUser.getName())
                .loginId(adminUser.getLoginId())
                .build();
    }

    @Transactional
    public void modifyAdminUserName(UserDetailsImpl userDetailsImpl, ModifyAdminNameReq modifyAdminNameReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        adminUser.updateName(modifyAdminNameReq.getAdminName());
    }

    @Transactional
    public void modifyAdminUserPassword(UserDetailsImpl userDetailsImpl, ModifyAdminPasswordReq modifyAdminPasswordReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        adminUser.updatePassword(passwordEncoder.encode(modifyAdminPasswordReq.getPassword()));
    }

    public CheckSecurityCodeRes checkSecurityCode(UserDetailsImpl userDetailsImpl, CheckSecurityCodeReq checkSecurityCodeReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        Admin admin = adminRepository.findByUser(adminUser)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 관리자가 존재하지 않습니다."));
        boolean valid = admin.getSecurityCode().equals(checkSecurityCodeReq.getSecurityCode());

        return CheckSecurityCodeRes.builder()
                .checked(valid)
                .build();
    }
}

