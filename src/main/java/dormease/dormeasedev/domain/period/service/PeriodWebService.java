package dormease.dormeasedev.domain.period.service;

import dormease.dormeasedev.domain.period.domain.Period;
import dormease.dormeasedev.domain.period.domain.repository.PeriodRepository;
import dormease.dormeasedev.domain.period.dto.request.PeriodReq;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
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
public class PeriodWebService {

    private final PeriodRepository periodRepository;

    private final UserService userService;

    // Description : 신청 기간 등록
    @Transactional
    public ResponseEntity<?> registerPeriod(CustomUserDetails customUserDetails, PeriodReq periodReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // TODO : 기간 테이블 요소들 관리 어떻게?
        //  생성 및 삭제에 관하여 / 현재에 적용되는 기간인지 구분 어떻게 할 것인지 등
        
        // Description : 해당 타입의 기간 존재 시, 삭제 후 생성 - 새로 신청 기간 생성하는 경우 기존 기간은 필요 없어지므로
        Optional<Period> findPeriod = periodRepository.findBySchoolAndPeriodType(school, periodReq.getPeriodType());
        if (findPeriod.isPresent()) {
            Period existPeriod = findPeriod.get();
            periodRepository.delete(existPeriod);
        }

        Period period = Period.builder()
                .school(school)
                .startDate(periodReq.getStartDate())
                .endDate(periodReq.getEndDate())
                .periodType(periodReq.getPeriodType())
                .build();

        periodRepository.save(period);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("신청 기간 등록이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
