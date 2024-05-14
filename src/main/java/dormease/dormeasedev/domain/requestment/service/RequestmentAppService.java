package dormease.dormeasedev.domain.requestment.service;

import dormease.dormeasedev.domain.requestment.domain.Progression;
import dormease.dormeasedev.domain.requestment.domain.Requestment;
import dormease.dormeasedev.domain.requestment.domain.repository.RequestmentRepository;
import dormease.dormeasedev.domain.requestment.dto.request.WriteRequestmentReq;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.resident.service.ResidentService;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Requestment.builder()
                .resident(resident)
                .school(school)
                .title(writeRequestmentReq.getTitle())
                .content(writeRequestmentReq.getContent())
                .isVisited()
                .isPublic()
                .progression(Progression.IN_REVIEW) // 검토 중
    }



}
