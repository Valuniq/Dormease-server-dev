package dormease.dormeasedev.domain.refund_requestment.service;

import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.refund_requestment.domain.respository.RefundRequestmentRepository;
import dormease.dormeasedev.domain.refund_requestment.dto.request.RefundRequestmentReq;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefundRequestmentAppService {

    private final RefundRequestmentRepository refundRequestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;

    // Description : 환불 신청
    @Transactional
    public ResponseEntity<?> requestRefund(CustomUserDetails customUserDetails, RefundRequestmentReq refundRequestmentReq) {

        // 신청 버튼 클릭 시 이미 신청 여부 반환
        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);
        School school = user.getSchool();

        DefaultAssert.isTrue(!refundRequestmentRepository.existsByResident(resident), "이미 환불 신청 하였습니다.");

        RefundRequestment refundRequestment = RefundRequestment.builder()
                .resident(resident)
                .school(school)
                .exitDate(refundRequestmentReq.getExitDate())
                .bankName(refundRequestmentReq.getBankName())
                .accountNumber(refundRequestmentReq.getAccountNumber())
                .build();
        refundRequestmentRepository.save(refundRequestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("환불 신청이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
