package dormease.dormeasedev.domain.school_settings.period.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.period.domain.Period;
import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import dormease.dormeasedev.domain.school_settings.period.domain.repository.PeriodRepository;
import dormease.dormeasedev.domain.school_settings.period.dto.request.PeriodReq;
import dormease.dormeasedev.domain.school_settings.period.dto.response.PeriodRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PeriodWebService {

    private final PeriodRepository periodRepository;

    private final UserService userService;

    // Description : 퇴사 / 환불 / 룸메이트 신청 기간 등록 / 수정
    @Transactional
    public ResponseEntity<?> registerPeriod(CustomUserDetails customUserDetails, PeriodReq periodReq) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // TODO : 기간 테이블 요소들 관리 어떻게?
        //  생성 및 삭제에 관하여 / 현재에 적용되는 기간인지 구분 어떻게 할 것인지 등
        LocalDate startDate = periodReq.getStartDate();
        LocalDate endDate = periodReq.getEndDate();
        DefaultAssert.isTrue(endDate.isAfter(startDate), "시작일이 마감일보다 과거여야 합니다.");

        Optional<Period> findPeriod = periodRepository.findBySchoolAndPeriodType(school, periodReq.getPeriodType());
        if (findPeriod.isPresent()) {
            Period existPeriod = findPeriod.get();
            existPeriod.updateDate(startDate, endDate);
        } else {
            Period period = Period.builder()
                    .school(school)
                    .startDate(startDate)
                    .endDate(endDate)
                    .periodType(periodReq.getPeriodType())
                    .build();

            periodRepository.save(period);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("신청 기간 등록 or 수정이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 퇴사 / 환불 / 룸메이트 신청 기간 조회
    public ResponseEntity<?> findPeriod(CustomUserDetails customUserDetails, PeriodType periodType) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        Period period = validatePeriodBySchoolAndPeriodType(school, periodType);
        PeriodRes periodRes = PeriodRes.builder()
                .periodId(period.getId())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .periodType(period.getPeriodType())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(periodRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public Period validatePeriodBySchoolAndPeriodType(School school, PeriodType periodType) {
        Optional<Period> findPeriod = periodRepository.findBySchoolAndPeriodType(school, periodType);
        DefaultAssert.isTrue(findPeriod.isPresent(), "해당 학교에 등록된 신청 기간이 존재하지 않습니다.");
        return findPeriod.get();
    }
}
