package dormease.dormeasedev.domain.school_settings.calendar.service;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Calendar;
import dormease.dormeasedev.domain.school_settings.calendar.domain.Color;
import dormease.dormeasedev.domain.school_settings.calendar.domain.repository.CalendarRepository;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.CalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.dto.response.CalendarDetailRes;
import dormease.dormeasedev.domain.school_settings.calendar.dto.response.CalendarRes;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.security.CustomUserDetails;
import dormease.dormeasedev.global.exception.DefaultAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserService userService;

    // 일정 등록
    // 종료일자 없으면 시작일자를 종료일자로 등록
    // 컬러는 디폴트 값 회색
    @Transactional
    public ResponseEntity<?> registerCalendar(CustomUserDetails customUserDetails, CalendarReq calendarReq) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Color color = selectDefaultColor(calendarReq);
        LocalDate endDate = selectDefaultEndDate(calendarReq);
        Calendar calendar = Calendar.builder()
                .school(admin.getSchool())
                .startDate(calendarReq.getStartDate())
                .endDate(endDate)
                .title(calendarReq.getTitle())
                .content(calendarReq.getContent())
                .color(color)
                .build();
        calendarRepository.save(calendar);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일정이 등록되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 일정 조회(년도 및 월별)
    public ResponseEntity<?> getCalendarsBySchoolAndYearAndMonth(CustomUserDetails customUserDetails, int year, int month) {
        User admin = userService.validateUserById(customUserDetails.getId());
        // 시작일로 정렬
        List<Calendar> calendars = calendarRepository.findBySchoolAndYearAndMonth(admin.getSchool(), year, month);

        List<CalendarRes> calendarResList = calendars.stream()
                .map(calendar -> CalendarRes.builder()
                        .calendarId(calendar.getId())
                        .startDate(calendar.getStartDate())
                        .endDate(calendar.getEndDate())
                        .title(calendar.getTitle())
                        .color(calendar.getColor())
                        .build()).
                toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 일정 상세 조회
    public ResponseEntity<?> getCalendarDetail(CustomUserDetails customUserDetails, Long calendarId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        CalendarDetailRes calendarDetailRes = CalendarDetailRes.builder()
                .calendarId(calendarId)
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .title(calendar.getTitle())
                .content(calendar.getContent())
                .color(calendar.getColor())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 일정 수정
    @Transactional
    public ResponseEntity<?> updateCalendar(CustomUserDetails customUserDetails, Long calendarId, CalendarReq calendarReq) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        Color color = selectDefaultColor(calendarReq);
        LocalDate endDate = selectDefaultEndDate(calendarReq);

        calendar.updateCalendar(calendarReq.getStartDate(), endDate, calendarReq.getTitle(), calendarReq.getContent(), color);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일정이 등록되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private Color selectDefaultColor(CalendarReq calendarReq) {
        return calendarReq.getColor() == null ? Color.GREY : calendarReq.getColor();
    }

    private LocalDate selectDefaultEndDate(CalendarReq calendarReq) {
        LocalDate endDate = calendarReq.getEndDate() == null ? calendarReq.getStartDate() : calendarReq.getEndDate();
        DefaultAssert.isTrue(calendarReq.getStartDate().isBefore(endDate) || calendarReq.getStartDate().isEqual(endDate), "시작일자는 종료일자와 같거나 먼저여야 합니다.");

        return endDate;
    }

    // 일정 삭제
    @Transactional
    public ResponseEntity<?> deleteCalendar(CustomUserDetails customUserDetails, Long calendarId) {
        User admin = userService.validateUserById(customUserDetails.getId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        calendarRepository.delete(calendar);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일정이 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public Calendar validCalendarById(Long calendarId) {
        Optional<Calendar> findCalendar = calendarRepository.findById(calendarId);
        DefaultAssert.isTrue(findCalendar.isPresent(), "일정 정보가 올바르지 않습니다.");
        return findCalendar.get();
    }
}
