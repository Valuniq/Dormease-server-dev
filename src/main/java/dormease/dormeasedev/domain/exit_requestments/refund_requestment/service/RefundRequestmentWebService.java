package dormease.dormeasedev.domain.exit_requestments.refund_requestment.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service.DormitoryApplicationService;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.RefundRequestment;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain.respository.RefundRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestments.refund_requestment.dto.response.RefundRequestmentRes;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.resident.service.ResidentService;
import dormease.dormeasedev.domain.dormitories.room.domain.Room;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.UserType;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageInfo;
import dormease.dormeasedev.global.payload.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefundRequestmentWebService {

    private final RefundRequestmentRepository refundRequestmentRepository;
    private final ResidentRepository residentRepository;

    private final UserService userService;
    private final ResidentService residentService;
    private final DormitoryApplicationService dormitoryApplicationService;

    // Description : 환불 신청 사생 목록 조회
    public ResponseEntity<?> findResidents(CustomUserDetails customUserDetails, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<RefundRequestment> refundRequestmentsBySchool = refundRequestmentRepository.findRefundRequestmentsByResident_User_School(school, pageable);

        List<RefundRequestment> refundRequestmentList = refundRequestmentsBySchool.getContent();
        List<RefundRequestmentRes> refundRequestmentResList = new ArrayList<>();
        for (RefundRequestment refundRequestment : refundRequestmentList) {
            // TODO : 사생 이름, 학번, 휴대전화, 은행명, 계좌번호, (거주)기간, 퇴사 예정일, 신청날짜, 건물(호실 포함), 호실, 침대번호
            Resident resident = refundRequestment.getResident();
            User user = resident.getUser();
            Room room = resident.getRoom();

            DormitoryApplication dormitoryApplication = dormitoryApplicationService.validateDormitoryApplicationByUserAndApplicationStatus(user, ApplicationStatus.NOW);
            Term term = dormitoryApplication.getTerm();
            Dormitory dormitory = dormitoryApplication.getDormitory();

            RefundRequestmentRes refundRequestmentRes = RefundRequestmentRes.builder()
                    .refundRequestmentId(refundRequestment.getId())
                    .residentName(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .phoneNumber(user.getPhoneNumber())
                    .bankName(refundRequestment.getBankName())
                    .accountNumber(refundRequestment.getAccountNumber())
                    .termName(term.getTermName())
                    .exitDate(refundRequestment.getExitDate())
                    .createDate(refundRequestment.getCreatedDate().toLocalDate())
                    .dormitoryName(dormitory.getName())
                    .roomSize(room.getRoomType().getRoomSize())    // 수정
                    .roomNumber(room.getRoomNumber())
                    .bedNumber(resident.getBedNumber())
                    .build();
            refundRequestmentResList.add(refundRequestmentRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, refundRequestmentsBySchool);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, refundRequestmentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 환불 신청한 사생 처리(삭제)
    @Transactional
    public ResponseEntity<?> deleteRefundRequestment(CustomUserDetails customUserDetails, Long refundRequestmentId) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        RefundRequestment refundRequestment = validateRefundRequestmentById(refundRequestmentId);
        Resident resident = refundRequestment.getResident();
        User user = resident.getUser();
        DefaultAssert.isTrue(user.getSchool().equals(school), "본인이 소속된 학교의 환불 요청만 처리할 수 있습니다.");

        // 삭제 시, refund_requestment-data.sql 삭제 + resident 삭제(user의 userType = USER 로 변경)
        refundRequestmentRepository.delete(refundRequestment);
        user.updateUserType(UserType.USER);
        residentRepository.delete(resident);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("해당 사생의 환불 신청 처리(삭제)가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public RefundRequestment validateRefundRequestmentById(Long refundRequestmentId) {
        Optional<RefundRequestment> findRefundRequestment = refundRequestmentRepository.findById(refundRequestmentId);
        DefaultAssert.isTrue(findRefundRequestment.isPresent(), "해당 아이디의 환불 요청이 존재하지 않습니다.");
        return findRefundRequestment.get();
    }
}
