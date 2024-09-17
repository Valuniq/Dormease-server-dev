package dormease.dormeasedev.domain.school_settings.period.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.period.domain.Period;
import dormease.dormeasedev.domain.school_settings.period.domain.PeriodType;
import dormease.dormeasedev.domain.school_settings.period.domain.repository.PeriodRepository;
import dormease.dormeasedev.domain.school_settings.period.dto.response.PeriodDateRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PeriodAppService {

    private final PeriodRepository periodRepository;

    private final UserService userService;

    // Description : 신청 기간 검증
    public ResponseEntity<?> validatePeriod(UserDetailsImpl userDetailsImpl, PeriodType periodType) {

        User user = userService.validateUserById(userDetailsImpl.getId());
        School school = user.getSchool();

        // 기간 검증
        Period period = validatePeriodBySchoolAndPeriodType(school, periodType);
        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        LocalDate now = LocalDate.now();

        // 현재보다 시작 날짜가 뒤 (시작 x) || 현재보다 종료 날짜가 앞 (끝) - 신청기간 x인지 여부 / true 시 신청기간 아님
        Boolean isPeriod = startDate.isAfter(now) || endDate.isBefore(now);

        PeriodDateRes periodDateRes = PeriodDateRes.builder()
                .isPeriod(!isPeriod)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(periodDateRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public Period validatePeriodBySchoolAndPeriodType(School school, PeriodType periodType) {
        Optional<Period> findPeriod = periodRepository.findBySchoolAndPeriodType(school, periodType);
        DefaultAssert.isTrue(findPeriod.isPresent(), "신청 기간이 존재하지 않습니다.");
        return findPeriod.get();
    }

}
