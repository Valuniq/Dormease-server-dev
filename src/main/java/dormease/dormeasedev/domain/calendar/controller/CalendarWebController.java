package dormease.dormeasedev.domain.calendar.controller;

import dormease.dormeasedev.domain.calendar.dto.request.CalendarReq;
import dormease.dormeasedev.domain.calendar.dto.response.CalendarDetailRes;
import dormease.dormeasedev.domain.calendar.dto.response.CalendarRes;
import dormease.dormeasedev.domain.calendar.service.CalendarService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Calendar API", description = "일정 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/calendar")
public class CalendarWebController {

    private final CalendarService calendarService;

    @Operation(summary = "일정 등록", description = "일정을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("")
    public ResponseEntity<?> registerCalendar(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 CalendarReq를 참고해주세요. 일정 등록 시 필요한 Request입니다.", required = true) @Valid @RequestBody CalendarReq calendarReq
    ) {
        return calendarService.registerCalendar(customUserDetails, calendarReq);
    }

    @Operation(summary = "일정 조회", description = "일정을 년도(Year)와 월(Month)로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CalendarRes.class)))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getCalendarsByYearAndMonth(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "조회하고자 하는 일정의 년도입니다.", required = true) @Positive @RequestParam int year,
            @Parameter(description = "조회하고자 하는 일정의 월입니다.", required = true) @Positive @RequestParam int month
    ) {
        return calendarService.getCalendarsBySchoolAndYearAndMonth(customUserDetails, year, month);
    }

    @Operation(summary = "일정 상세 조회", description = "일정을 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CalendarDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{calendarId}")
    public ResponseEntity<?> getCalendarDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "일정의 id를 입력해주세요.", required = true) @PathVariable Long calendarId
    ) {
        return calendarService.getCalendarDetail(customUserDetails, calendarId);
    }

    @Operation(summary = "일정 수정", description = "일정을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PutMapping("/{calendarId}")
    public ResponseEntity<?> updateCalendarDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "일정의 id를 입력해주세요.", required = true) @PathVariable Long calendarId,
            @Parameter(description = "Schemas의 CalendarReq를 참고해주세요. 일정 수정 시 필요한 Request입니다.", required = true) @RequestBody CalendarReq calendarReq
    ) {
        return calendarService.updateCalendar(customUserDetails, calendarId, calendarReq);
    }

    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<?> deleteCalendarDetail(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "일정의 id를 입력해주세요.", required = true) @PathVariable Long calendarId
    ) {
        return calendarService.deleteCalendar(customUserDetails, calendarId);
    }
}