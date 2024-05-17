package dormease.dormeasedev.domain.requestment.service;

import dormease.dormeasedev.domain.requestment.domain.Progression;
import dormease.dormeasedev.domain.requestment.domain.Requestment;
import dormease.dormeasedev.domain.requestment.domain.repository.RequestmentRepository;
import dormease.dormeasedev.domain.requestment.dto.request.WriteRequestmentReq;
import dormease.dormeasedev.domain.requestment.dto.response.RequestmentRes;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RequestmentAppService {

    private final RequestmentRepository requestmentRepository;

    private final UserService userService;
    private final ResidentService residentService;

    // Description : 요청사항 작성
    @Transactional
    public ResponseEntity<?> writeRequestment(CustomUserDetails customUserDetails, WriteRequestmentReq writeRequestmentReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        Resident resident = residentService.validateResidentByUser(user);
        School school = user.getSchool();

        Requestment requestment = Requestment.builder()
                .resident(resident)
                .school(school)
                .title(writeRequestmentReq.getTitle())
                .content(writeRequestmentReq.getContent())
                .consentDuringAbsence(writeRequestmentReq.getConsentDuringAbsence()) // 부재중 방문 동의 여부
                .visibility(writeRequestmentReq.getVisibility()) // 공개 여부
                .progression(Progression.IN_REVIEW) // 검토 중
                .build();

        requestmentRepository.save(requestment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("요청사항 작성이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 요청사항 목록 조회
    public ResponseEntity<?> findRequestments(CustomUserDetails customUserDetails, Integer page) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "createdDate"));// 최신순.. 기능 정의서에는 날짜순이라는데 걍 최신으로 함
        Page<Requestment> requestmentPage = requestmentRepository.findRequestmentsBySchool(school, pageable);

        List<Requestment> requestmentList = requestmentPage.getContent();
        List<RequestmentRes> requestmentResList = new ArrayList<>();
        for (Requestment requestment : requestmentList) {
            Resident resident = requestment.getResident();
            User student = resident.getUser();

            RequestmentRes requestmentRes = RequestmentRes.builder()
                    .requestmentId(requestment.getId())
                    .title(requestment.getTitle())
                    .writer(student.getName())
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
}
