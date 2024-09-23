package dormease.dormeasedev.domain.school_settings.calendar.service;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Calendar;
import dormease.dormeasedev.domain.school_settings.calendar.domain.Color;
import dormease.dormeasedev.domain.school_settings.calendar.domain.repository.CalendarRepository;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.CreateCalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.UpdateCalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.dto.response.CalendarDetailRes;
import dormease.dormeasedev.domain.school_settings.calendar.dto.response.CalendarRes;
import dormease.dormeasedev.domain.school_settings.calendar.dto.response.TodayCalendarRes;
import dormease.dormeasedev.domain.school_settings.calendar.mapper.CalendarMapper;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper;
    private final UserService userService;

    // 일정 등록
    // 컬러는 디폴트 값 회색
    @Transactional
    public Calendar registerCalendar(UserDetailsImpl userDetailsImpl, CreateCalendarReq createCalendarReq) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        Color color = createCalendarReq.getColor() == null ? Color.GREY : createCalendarReq.getColor();

        Calendar calendar = Calendar.builder()
                .school(admin.getSchool())
                .startDate(createCalendarReq.getStartDate())
                .endDate(createCalendarReq.getEndDate())
                .title(createCalendarReq.getTitle())
                .content(createCalendarReq.getContent())
                .color(color)
                .build();
        calendarRepository.save(calendar);
        return calendar;
    }

    // 일정 조회(년도 및 월별)
    public List<CalendarRes> getCalendarsBySchoolAndYearAndMonth(UserDetailsImpl userDetailsImpl, int year, int month) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        // 시작일로 정렬
        List<Calendar> calendars = calendarRepository.findBySchoolAndYearAndMonth(admin.getSchool(), year, month);

        return calendars.stream()
                .map(calendar -> CalendarRes.builder()
                        .calendarId(calendar.getId())
                        .startDate(calendar.getStartDate())
                        .endDate(calendar.getEndDate())
                        .title(calendar.getTitle())
                        .color(calendar.getColor())
                        .build()).
                toList();
    }

    // 일별 일정 목록 조회
    public List<TodayCalendarRes> getCalendarsBySchoolAndDate(UserDetailsImpl userDetailsImpl, LocalDate date) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        List<Calendar> calendars = calendarRepository.findCalendarsByDate(admin.getSchool(), date);

        return calendars.stream()
                .map(calendar -> TodayCalendarRes.builder()
                        .calendarId(calendar.getId())
                        .title(calendar.getTitle())
                        .color(calendar.getColor())
                        .build())
                .collect(Collectors.toList());
    }

    // 일정 상세 조회
    public CalendarDetailRes getCalendarDetail(UserDetailsImpl userDetailsImpl, Long calendarId) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        return CalendarDetailRes.builder()
                .calendarId(calendarId)
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .title(calendar.getTitle())
                .content(calendar.getContent())
                .color(calendar.getColor())
                .build();
    }

    // 일정 수정
    @Transactional
    public void updateCalendar(UserDetailsImpl userDetailsImpl, Long calendarId, UpdateCalendarReq updateCalendarReq) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        calendarMapper.updateCalender(updateCalendarReq, calendar);
    }

    // 일정 삭제
    @Transactional
    public void deleteCalendar(UserDetailsImpl userDetailsImpl, Long calendarId) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        Calendar calendar = validCalendarById(calendarId);
        DefaultAssert.isTrue(admin.getSchool() == calendar.getSchool(), "관리자의 학교에 소속된 일정이 아닙니다.");

        calendarRepository.delete(calendar);
    }

    public Calendar validCalendarById(Long calendarId) {
        Optional<Calendar> findCalendar = calendarRepository.findById(calendarId);
        DefaultAssert.isTrue(findCalendar.isPresent(), "일정 정보가 올바르지 않습니다.");
        return findCalendar.get();
    }
}
