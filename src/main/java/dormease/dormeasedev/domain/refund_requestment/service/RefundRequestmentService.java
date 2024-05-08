package dormease.dormeasedev.domain.refund_requestment.service;

import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.refund_requestment.domain.respository.RefundRequestmentRepository;
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
public class RefundRequestmentService {

    private final RefundRequestmentRepository refundRequestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;

    // Description : 환불 신청 사생 목록 조회
    public ResponseEntity<?> findResidents(CustomUserDetails customUserDetails) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // TODO : 학교 - 룸메이트신청, 룸메이트임시신청, 요청사항, 환불신청, 퇴사신청 연결 필요할듯?
    }

    // Description : 환불 신청한 사생 처리(삭제)
    @Transactional
    public ResponseEntity<?> deleteResident(Long residentId) {

        Resident resident = residentService.validateResidentById(residentId);
        Optional<RefundRequestment> findRefundRequestment = refundRequestmentRepository.findByResident(resident);
        DefaultAssert.isTrue(findRefundRequestment.isPresent(), "해당 id를 가진 사생의 환불 신청이 존재하지 않습니다.");
        RefundRequestment refundRequestment = findRefundRequestment.get();

        refundRequestmentRepository.delete(refundRequestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("해당 사생의 환불 신청 처리(삭제)가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
