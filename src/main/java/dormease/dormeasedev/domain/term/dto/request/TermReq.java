package dormease.dormeasedev.domain.term.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryReq;
import dormease.dormeasedev.domain.dormitory_term.dto.DormitoryTermReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class TermReq {

    // 거주 기간 - 학기, 6개월 등 ..
    @Schema(type = "String", example = "학기", description= "거주 기간 이름입니다.")
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

    @Schema(type = "List<DormitoryTermReq>", example = "dormitoryTermReqList", description= "기숙사와 거주기간 중간 테이블을 위한 리스트입니다. 거주기간과 연관된 기숙사 목록입니다.")
    private List<DormitoryTermReq> dormitoryTermReqList = new ArrayList<>();
}
