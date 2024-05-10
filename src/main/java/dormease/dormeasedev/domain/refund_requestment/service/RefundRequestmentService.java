package dormease.dormeasedev.domain.refund_requestment.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_application.service.DormitoryApplicationService;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.refund_requestment.domain.respository.RefundRequestmentRepository;
import dormease.dormeasedev.domain.refund_requestment.dto.response.RefundRequestmentRes;
import dormease.dormeasedev.domain.refund_requestment.dto.response.RefundRequestmentResWithPage;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefundRequestmentService {

    private final RefundRequestmentRepository refundRequestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 환불 신청 사생 목록 조회
    public ResponseEntity<?> findResidents(CustomUserDetails customUserDetails, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<RefundRequestment> refundRequestmentsBySchool = refundRequestmentRepository.findRefundRequestmentsBySchool(school, pageable);
        Integer totalPage = refundRequestmentsBySchool.getTotalPages();

        List<RefundRequestment> refundRequestmentList = refundRequestmentsBySchool.getContent();
        List<RefundRequestmentRes> refundRequestmentResList = new ArrayList<>();
        for (RefundRequestment refundRequestment : refundRequestmentList) {
            // TODO : 사생 이름, 학번, 휴대전화, 은행명, 계좌번호, (거주)기간, 퇴사 예정일, 신청날짜, 건물(호실 포함), 호실, 침대번호
            Resident resident = refundRequestment.getResident();
            User user = resident.getUser();
            Room room = resident.getRoom();

            DormitoryApplication dormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(user, ApplicationStatus.NOW);
            DormitoryTerm dormitoryTerm = dormitoryApplication.getDormitoryTerm();
            Dormitory dormitory = dormitoryTerm.getDormitory();

            RefundRequestmentRes refundRequestmentRes = RefundRequestmentRes.builder()
                    .refundRequestmentId(refundRequestment.getId())
                    .residentName(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .phoneNumber(user.getPhoneNumber())
                    .bankName(resident.getBankName())
                    .accountNumber(resident.getAccountNumber())
                    .term(dormitoryTerm.getTerm())
                    .exitDate(refundRequestment.getExitDate())
                    .createDate(refundRequestment.getCreatedDate().toLocalDate())
                    .dormitoryName(dormitory.getName())
                    .roomSize(dormitory.getRoomSize())
                    .roomNumber(room.getRoomNumber())
                    .bedNumber(resident.getBedNumber())
                    .build();
            refundRequestmentResList.add(refundRequestmentRes);
        }

        RefundRequestmentResWithPage refundRequestmentResWithPage = RefundRequestmentResWithPage.builder()
                .totalPage(totalPage)
                .refundRequestmentResList(refundRequestmentResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(refundRequestmentResWithPage)
                .build();

        return ResponseEntity.ok(apiResponse);
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
