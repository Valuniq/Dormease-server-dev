package dormease.dormeasedev.domain.exit_requestments.refund_requestment.service;

import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.respository.RefundRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.request.RefundRequestmentReq;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        DefaultAssert.isTrue(!refundRequestmentRepository.existsByResident(resident), "이미 환불 신청 하였습니다.");

        RefundRequestment refundRequestment = RefundRequestment.builder()
                .resident(resident)
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
