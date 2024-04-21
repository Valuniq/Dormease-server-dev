package dormease.dormeasedev.domain.dormitory_application.service;

import dormease.dormeasedev.domain.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_application.dto.request.DormitoryApplicationReq;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationService {

    private final DormitoryApplicationRepository dormitoryApplicationRepository;

//    // Description : 입사 신청
//    @Transactional
//    public ResponseEntity<?> dormitoryApplication(CustomUserDetails customUserDetails, DormitoryApplicationReq dormitoryApplicationReq) {
//
//
//    }
}
