package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.response.DormitoryTermRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TermRes {

    @Schema(type = "Long", example = "1", description= "거주 기간 id입니다.")
    private Long termId;

    // 거주 기간 - 학기, 6개월 등 ..
    @Schema(type = "String", example = "학기", description= "거주 기간입니다.")
    private String termName;

    // 입퇴사 날짜 - 시작일
    @Schema(type = "local date", example = "2023-12-25", description = "입퇴사 날짜 - 시작일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate startDate;

    // 입퇴사 날짜 - 마감일
    @Schema(type = "local date", example = "2024-03-15", description = "입퇴사 날짜 - 마감일")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate endDate;

    @Schema(type = "DormitoryTermRes", example = "dormitoryTermResList 객체", description= "기숙사와 거주기간 관련 리스트입니다.")
    private List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();
}
