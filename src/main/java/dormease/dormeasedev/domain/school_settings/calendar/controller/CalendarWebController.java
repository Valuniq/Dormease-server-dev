package dormease.dormeasedev.domain.school_settings.calendar.controller;

import dormease.dormeasedev.domain.school_settings.calendar.domain.Calendar;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.CreateCalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.dto.request.UpdateCalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.service.CalendarService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/calendar")
public class CalendarWebController implements CalendarWebApi {

    private final CalendarService calendarService;

    @Override
    @PostMapping("")
    public ResponseEntity<Void> registerCalendar(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody CreateCalendarReq createCalendarReq
    ) {
        Calendar calendar = calendarService.registerCalendar(userDetailsImpl, createCalendarReq);
        return ResponseEntity.created(URI.create("/api/v1/web/calendar/" + calendar.getId())).build();
    }

    @Override
    @GetMapping("")
    public ResponseEntity<ApiResponse> getCalendarsByYearAndMonth(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Positive @RequestParam int year,
            @Positive @RequestParam int month
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarService.getCalendarsBySchoolAndYearAndMonth(userDetailsImpl, year, month))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse> getCalendarsByDate(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable LocalDate date
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarService.getCalendarsBySchoolAndDate(userDetailsImpl, date))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/today")
    public ResponseEntity<ApiResponse> getTodayCalendars(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        LocalDate today = LocalDate.now();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarService.getCalendarsBySchoolAndDate(userDetailsImpl, today))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{calendarId}")
    public ResponseEntity<ApiResponse> getCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarService.getCalendarDetail(userDetailsImpl, calendarId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @PutMapping("/{calendarId}")
    public ResponseEntity<Void> updateCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId,
            @Valid @RequestBody UpdateCalendarReq updateCalendarReq
    ) {
        calendarService.updateCalendar(userDetailsImpl, calendarId, updateCalendarReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<Void> deleteCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId
    ) {
        calendarService.deleteCalendar(userDetailsImpl, calendarId);
        return ResponseEntity.noContent().build();
    }
}
