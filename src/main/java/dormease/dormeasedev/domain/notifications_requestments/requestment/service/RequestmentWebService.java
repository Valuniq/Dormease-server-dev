package dormease.dormeasedev.domain.notifications_requestments.requestment.service;

import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.Requestment;
import dormease.dormeasedev.domain.notifications_requestments.requestment.domain.repository.RequestmentRepository;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.request.ModifyProgressionReq;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentDetailAdminRes;
import dormease.dormeasedev.domain.notifications_requestments.requestment.dto.response.RequestmentRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
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
public class RequestmentWebService {

    private final UserService userService;
    private final RequestmentAppService requestmentAppService;

    private final RequestmentRepository requestmentRepository;

    // Description : 요청사항 목록 조회 (무한 스크롤)
    public PageResponse findRequestments(CustomUserDetails customUserDetails, Integer page) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();

        Pageable pageable = PageRequest.of(page, 13, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Requestment> requestmentPage = requestmentRepository.findRequestmentsByUser_School(school, pageable);

        List<Requestment> requestmentList = requestmentPage.getContent();
        List<RequestmentRes> requestmentResList = new ArrayList<>();
        for (Requestment requestment : requestmentList) {
            User student = requestment.getUser();

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
        return PageResponse.toPageResponse(pageInfo, requestmentResList);
    }

    // Description : 요청사항 상세 조회
    public RequestmentDetailAdminRes findRequestment(CustomUserDetails customUserDetails, Long requestmentId) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();
        Requestment requestment = requestmentAppService.validateRequestmentByIdAndSchool(requestmentId, school); // 본인 학교 요청사항만 조회 가능

        User requestmentUser = requestment.getUser();

        return RequestmentDetailAdminRes.builder()
                .requestmentId(requestmentId)
                .title(requestment.getTitle())
                .content(requestment.getContent())
                .writer(requestmentUser.getName())
                .createdDate(requestment.getCreatedDate().toLocalDate())
                .consentDuringAbsence(requestment.getConsentDuringAbsence())
                .visibility(requestment.getVisibility())
                .progression(requestment.getProgression())
                .build();
    }

    @Transactional
    public void modifyProgression(CustomUserDetails customUserDetails, Long requestmentId, ModifyProgressionReq modifyProgressionReq) {

        User admin = userService.validateUserById(customUserDetails.getId());
        School school = admin.getSchool();
        Requestment requestment = requestmentAppService.validateRequestmentByIdAndSchool(requestmentId, school);

        requestment.updateProgression(modifyProgressionReq.getProgression());
    }
}
