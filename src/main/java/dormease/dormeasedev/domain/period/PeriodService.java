package dormease.dormeasedev.domain.period;

import dormease.dormeasedev.domain.period.domain.Period;
import dormease.dormeasedev.domain.period.domain.PeriodType;
import dormease.dormeasedev.domain.period.domain.repository.PeriodRepository;
import dormease.dormeasedev.domain.period.dto.response.PeriodDateRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.service.UserService;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PeriodService {

    private final PeriodRepository periodRepository;

    private final UserService userService;

    // Description : 룸메이트 신청 기간 검증
    public ResponseEntity<?> validateRoommateTempApplicationPeriod(CustomUserDetails customUserDetails, PeriodType periodType) {

        User user = userService.validateUserById(customUserDetails.getId());
        School school = user.getSchool();

        // 기간 검증
        Period period = validatePeriodBySchoolAndPeriodType(school, periodType);
        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        LocalDate now = LocalDate.now();

        // 현재보다 시작 날짜가 뒤 (시작 x) || 현재보다 종료 날짜가 앞 (끝) - 신청기간 x인지 여부 / true 시 신청기간 아님
        Boolean isOverPeriod = startDate.isAfter(now) || endDate.isBefore(now);

        PeriodDateRes periodDateRes = PeriodDateRes.builder()
                .isOverPeriod(!isOverPeriod)
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
        DefaultAssert.isTrue(findPeriod.isPresent(), "룸메이트 신청 기간이 존재하지 않습니다.");
        return findPeriod.get();
    }
}
