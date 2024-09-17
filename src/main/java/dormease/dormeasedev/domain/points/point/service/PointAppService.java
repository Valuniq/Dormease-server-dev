package dormease.dormeasedev.domain.points.point.service;

import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.points.point.domain.PointType;
import dormease.dormeasedev.domain.points.point.domain.repository.PointRepository;
import dormease.dormeasedev.domain.points.point.dto.response.UserPointAppRes;
import dormease.dormeasedev.domain.points.point.dto.response.UserPointHistoryAppRes;
import dormease.dormeasedev.domain.points.user_point.domain.UserPoint;
import dormease.dormeasedev.domain.points.user_point.domain.repository.UserPointRepository;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.StudentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PointAppService {

    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;
    private final StudentRepository studentRepository;

    private final UserService userService;

    // Description : 회원 상벌점 조회
    public ResponseEntity<?> getUserPointTotal(UserDetailsImpl userDetailsImpl) {
        Student student = studentRepository.findById(userDetailsImpl.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 회원이 존재하지 않습니다."));

        UserPointAppRes userPointAppRes = UserPointAppRes.builder()
                .bonusPoint(student.getBonusPoint())
                .minusPoint(student.getMinusPoint())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointAppRes)
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    // Description : 회원 상점/벌점 내역 조회
    public ResponseEntity<?> getUserPointHistory(UserDetailsImpl userDetailsImpl, String type) {
        User user = userRepository.getReferenceById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 학생이 아닙니다."));
        List<UserPoint> userPoints = userPointRepository.findUserPointsByStudentAndPoint_pointTypeOrderByCreatedDateDesc(student, PointType.valueOf(type));

        List<UserPointHistoryAppRes> userPointHistoryAppResList = userPoints.stream()
                .map(userPoint -> UserPointHistoryAppRes.builder()
                        .userPointId(userPoint.getId())
                        .createdDate(userPoint.getCreatedDate().toLocalDate())
                        .content(userPoint.getPoint().getContent())
                        .score(userPoint.getPoint().getScore())
                        .build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userPointHistoryAppResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
