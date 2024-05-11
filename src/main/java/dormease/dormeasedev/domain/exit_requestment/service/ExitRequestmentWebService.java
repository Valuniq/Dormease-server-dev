package dormease.dormeasedev.domain.exit_requestment.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.exit_requestment.domain.ExitRequestment;
import dormease.dormeasedev.domain.exit_requestment.domain.repository.ExitRequestmentRepository;
import dormease.dormeasedev.domain.exit_requestment.dto.request.DeleteExitRequestmentReq;
import dormease.dormeasedev.domain.exit_requestment.dto.request.ModifyDepositReq;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ExitRequestmentRes;
import dormease.dormeasedev.domain.exit_requestment.dto.response.ExitRequestmentResidentRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
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
public class ExitRequestmentWebService {

    private final ExitRequestmentRepository exitRequestmentRepository;

    private final UserService userService;

    // Description : 퇴사 신청 사생 목록 조회
    public ResponseEntity<?> findResidents(CustomUserDetails customUserDetails, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 23, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ExitRequestment> exitRequestmentsBySchool = exitRequestmentRepository.findExitRequestmentsBySchool(school, pageable);

        List<ExitRequestment> exitRequestmentList = exitRequestmentsBySchool.getContent();
        List<ExitRequestmentResidentRes> exitRequestmentResidentResList = new ArrayList<>();
        for (ExitRequestment exitRequestment : exitRequestmentList) {
            Resident resident = exitRequestment.getResident();
            User user = resident.getUser();
            Room room = resident.getRoom();
            Dormitory dormitory = room.getDormitory();

            ExitRequestmentResidentRes exitRequestmentResidentRes = ExitRequestmentResidentRes.builder()
                    .exitRequestmentId(exitRequestment.getId())
                    .residentName(user.getName())
                    .studentNumber(user.getStudentNumber())
                    .dormitoryName(dormitory.getName())
                    .roomSize(dormitory.getRoomSize())
                    .roomNumber(room.getRoomNumber())
                    .exitDate(exitRequestment.getExitDate())
                    .hasKey(exitRequestment.getHasKey())
                    .createDate(exitRequestment.getCreatedDate().toLocalDate())
                    .securityDepositReturnStatus(exitRequestment.getSecurityDepositReturnStatus())
                    .build();
            exitRequestmentResidentResList.add(exitRequestmentResidentRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, exitRequestmentsBySchool);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, exitRequestmentResidentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 퇴사 신청서 조회
    public ResponseEntity<?> findExitRequestment(Long exitRequestmentId) {

        ExitRequestment exitRequestment = validateExitRequestmentById(exitRequestmentId);
        Resident resident = exitRequestment.getResident();
        User user = resident.getUser();

        Room room = resident.getRoom();
        Dormitory dormitory = room.getDormitory();

        ExitRequestmentRes exitRequestmentRes = ExitRequestmentRes.builder()
                .exitRequestmentId(exitRequestmentId)
                .residentName(user.getName())
                .major(user.getMajor())
                .studentNumber(user.getStudentNumber())
                .schoolYear(user.getSchoolYear())
                .phoneNumber(user.getPhoneNumber())
                .dormitoryName(dormitory.getName())
                .roomSize(dormitory.getRoomSize())
                .securityDepositReturnStatus(exitRequestment.getSecurityDepositReturnStatus())
                .roomNumber(room.getRoomNumber())
                .bedNumber(resident.getBedNumber())
                .hasKey(exitRequestment.getHasKey())
                .keyNumber(exitRequestment.getKeyNumber())
                .exitDate(exitRequestment.getExitDate())
                .bankName(exitRequestment.getBankName())
                .accountNumber(exitRequestment.getAccountNumber())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(exitRequestmentRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 보증금 환급 상태 변경
    @Transactional
    public ResponseEntity<?> modifySecurityDeposit(ModifyDepositReq modifyDepositReq) {

        List<Long> exitRequestmentIdList = modifyDepositReq.getExitRequestmentIdList();
        for (Long exitRequestmentId : exitRequestmentIdList) {
            ExitRequestment exitRequestment = validateExitRequestmentById(exitRequestmentId);
            exitRequestment.updateSecurityDepositReturnStatus(modifyDepositReq.getSecurityDepositReturnStatus());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("보증금 환급 상태가 변경되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 퇴사 신청서 삭제
    @Transactional
    public ResponseEntity<?> deleteExitRequestment(DeleteExitRequestmentReq deleteExitRequestmentReq) {

        List<Long> exitRequestmentIdList = deleteExitRequestmentReq.getExitRequestmentIdList();
        List<ExitRequestment> exitRequestmentList = new ArrayList<>();
        for (Long exitRequestmentId : exitRequestmentIdList) {
            ExitRequestment exitRequestment = validateExitRequestmentById(exitRequestmentId);
            exitRequestmentList.add(exitRequestment);
        }
        exitRequestmentRepository.deleteAll(exitRequestmentList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("퇴사 신청서 삭제가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // Description : 유효성 검증 함수
    public ExitRequestment validateExitRequestmentById(Long exitRequestmentId) {
        Optional<ExitRequestment> findExitRequestment = exitRequestmentRepository.findById(exitRequestmentId);
        DefaultAssert.isTrue(findExitRequestment.isPresent(), "해당 id의 퇴사 신청이 존재하지 않습니다.");
        return findExitRequestment.get();
    }

}
