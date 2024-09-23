package dormease.dormeasedev.domain.school_settings.calendar.controller;

import dormease.dormeasedev.domain.school_settings.calendar.dto.request.CalendarReq;
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
        Long calendarId = calendarService.registerCalendar(userDetailsImpl, calendarReq);
        return ResponseEntity.created(URI.create("/api/v1/web/calendar/" + calendarId)).build();
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
    @PatchMapping("/{calendarId}")
    public ResponseEntity<?> updateCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId,
            @RequestBody CalendarReq calendarReq
    ) {
        calendarService.updateCalendar(userDetailsImpl, calendarId, calendarReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<?> deleteCalendarDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long calendarId
    ) {
        calendarService.deleteCalendar(userDetailsImpl, calendarId);
        return ResponseEntity.noContent().build();
    }
}
