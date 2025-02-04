package dormease.dormeasedev.domain.notifications_requestments.requestment.service;

import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.Requestment;
import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.repository.RequestmentRepository;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request.WriteRequestmentReq;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentDetailUserRes;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.resident.domain.repository.ResidentRepository;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.student.domain.repository.StudentRepository;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.domain.repository.UserRepository;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageInfo;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
public class RequestmentAppService {

    private final RequestmentRepository requestmentRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ResidentRepository residentRepository;

    private final UserService userService;

    // Description : 요청사항 작성
    @Transactional
    public ResponseEntity<?> writeRequestment(UserDetailsImpl userDetailsImpl, WriteRequestmentReq writeRequestmentReq) {

        User user = userRepository.getReferenceById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다."));

        if (!residentRepository.existsByStudent(student))
            throw new IllegalArgumentException("사생만 요청사항을 작성할 수 있습니다.");

        Requestment requestment = Requestment.builder()
                .student(student)
                .title(writeRequestmentReq.getTitle())
                .content(writeRequestmentReq.getContent())
                .consentDuringAbsence(writeRequestmentReq.getConsentDuringAbsence()) // 부재중 방문 동의 여부
                .visibility(writeRequestmentReq.getVisibility()) // 공개 여부
                .build();

        requestmentRepository.save(requestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(
                        Message.builder()
                                .message("요청사항 작성이 완료되었습니다.")
                                .build()
                )
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 요청사항 목록 조회
    public ResponseEntity<?> findRequestments(UserDetailsImpl userDetailsImpl, Integer page) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();

        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdDate")); // 최신순.. 기능 정의서에는 날짜순이라는데 걍 최신으로 함
        Page<Requestment> requestmentPage = requestmentRepository.findRequestmentsByStudent_User_SchoolAndVisibility(school, true, pageable);

        List<Requestment> requestmentList = requestmentPage.getContent();
        List<RequestmentRes> requestmentResList = new ArrayList<>();
        for (Requestment requestment : requestmentList) {
            User requestmentUser = requestment.getStudent().getUser();

            RequestmentRes requestmentRes = RequestmentRes.builder()
                    .requestmentId(requestment.getId())
                    .title(requestment.getTitle())
                    .writer(requestmentUser.getName())
                    .createdDate(requestment.getCreatedDate().toLocalDate())
                    .progression(requestment.getProgression())
                    .build();
            requestmentResList.add(requestmentRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, requestmentPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, requestmentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 내 요청사항 목록 조회
    public ResponseEntity<?> findMyRequestments(UserDetailsImpl userDetailsImpl, Integer page) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(IllegalArgumentException::new);

        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Requestment> requestmentPage = requestmentRepository.findRequestmentsByStudent(student, pageable);

        List<Requestment> requestmentList = requestmentPage.getContent();
        List<RequestmentRes> requestmentResList = new ArrayList<>();
        for (Requestment requestment : requestmentList) {
            RequestmentRes requestmentRes = RequestmentRes.builder()
                    .requestmentId(requestment.getId())
                    .title(requestment.getTitle())
                    .writer(user.getName())
                    .createdDate(requestment.getCreatedDate().toLocalDate())
                    .progression(requestment.getProgression())
                    .build();
            requestmentResList.add(requestmentRes);
        }

        PageInfo pageInfo = PageInfo.toPageInfo(pageable, requestmentPage);
        PageResponse pageResponse = PageResponse.toPageResponse(pageInfo, requestmentResList);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(pageResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 요청사항 상세 조회
    public ResponseEntity<?> findRequestmentDetail(UserDetailsImpl userDetailsImpl, Long requestmentId) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();
        Requestment requestment = validateRequestmentByIdAndSchool(requestmentId, school);

        User requestmentUser = requestment.getStudent().getUser();
        Boolean myRequestment = user.equals(requestmentUser);

        RequestmentDetailUserRes requestmentDetailUserRes = RequestmentDetailUserRes.builder()
                .requestmentId(requestmentId)
                .myRequestment(myRequestment)
                .title(requestment.getTitle())
                .content(requestment.getContent())
                .writer(requestmentUser.getName())
                .createdDate(requestment.getCreatedDate().toLocalDate())
                .consentDuringAbsence(requestment.getConsentDuringAbsence())
                .visibility(requestment.getVisibility())
                .progression(requestment.getProgression())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(requestmentDetailUserRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 요청사항 삭제
    @Transactional
    public ResponseEntity<?> deleteRequestment(UserDetailsImpl userDetailsImpl, Long requestmentId) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        Student student = studentRepository.findByUser(user)
                .orElseThrow(IllegalArgumentException::new);
        Requestment requestment = validateRequestmentByIdAndUser(requestmentId, student);

        requestmentRepository.delete(requestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("요청사항 삭제가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    

    // Description : 유효성 검증 함수
    public Requestment validateRequestmentByIdAndSchool(Long requestmentId, School school) {
        Optional<Requestment> findRequestment = requestmentRepository.findByIdAndStudent_User_School(requestmentId, school);
        DefaultAssert.isTrue(findRequestment.isPresent(), "올바르지 않은 요청사항 ID입니다.");
        return findRequestment.get();
    }

    private Requestment validateRequestmentByIdAndUser(Long requestmentId, Student student) {
        Optional<Requestment> findRequestment = requestmentRepository.findByIdAndStudent(requestmentId, student);
        DefaultAssert.isTrue(findRequestment.isPresent(), "올바르지 않은 요청사항 ID입니다.");
        return findRequestment.get();
    }
}
