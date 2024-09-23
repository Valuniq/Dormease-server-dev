package dormease.dormeasedev.domain.school_settings.calendar.controller;

import dormease.dormeasedev.domain.school_settings.calendar.dto.request.CalendarReq;
import dormease.dormeasedev.domain.school_settings.calendar.service.CalendarService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/calendar")
public class CalendarWebController implements CalendarWebApi {

    private final CalendarService calendarService;

    @Override
    @PostMapping("")
    public ResponseEntity<?> registerCalendar(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Valid @RequestBody CalendarReq calendarReq
    ) {
        return calendarService.registerCalendar(userDetailsImpl, calendarReq);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<?> getCalendarsByYearAndMonth(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Positive @RequestParam int year,
            @Positive @RequestParam int month
    ) {
        return calendarService.getCalendarsBySchoolAndYearAndMonth(userDetailsImpl, year, month);
    }

    @Override
    @GetMapping("/{calendarId}")
    public ResponseEntity<?> getCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId
    ) {
        return calendarService.getCalendarDetail(userDetailsImpl, calendarId);
    }

    @Override
    @PutMapping("/{calendarId}")
    public ResponseEntity<?> updateCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId,
            @RequestBody CalendarReq calendarReq
    ) {
        return calendarService.updateCalendar(userDetailsImpl, calendarId, calendarReq);
    }

    @Override
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<?> deleteCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId
    ) {
        return calendarService.deleteCalendar(userDetailsImpl, calendarId);
    }
}
