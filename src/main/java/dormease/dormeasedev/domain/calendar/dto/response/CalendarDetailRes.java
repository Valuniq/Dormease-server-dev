package dormease.dormeasedev.domain.calendar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CalendarDetailRes {

    @Schema(type = "Long", example = "1", description= "일정 id입니다.")
    private Long calendarId;

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 시작일자입니다.")
    private LocalDate startDate;

    @Schema(type = "LocalDate", example = "2024-07-10", description= "일정의 종료일자입니다.")
    private LocalDate endDate;

    @Schema(type = "String", example = "수강신청", description= "일정의 제목입니다.")
    private String title;

    @Schema(type = "String", example = "9일: 인문대학, 10일: ICT융합대학/경영대학, 11일: 법과대학/사회과학대학", description= "일정의 내용입니다.")
    private String content;

    @Schema(type = "String", example = "GREY, RED, GREEN, YELLOW, BLUE", description= "일정의 색깔입니다.")
    private String color;


    @Builder
    public CalendarDetailRes(Long calendarId, LocalDate startDate, LocalDate endDate, String title, String content, String color) {
        this.calendarId = calendarId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.color = color;
    }
}
