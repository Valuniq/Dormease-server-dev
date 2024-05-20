package dormease.dormeasedev.domain.dormitory.dto.request;

import dormease.dormeasedev.domain.dormitory_term.dto.request.DormitoryTermReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class DormitoryReq {

    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    // TODO : 바꾸기
    @Schema(type = "Integer", example = "250", description= "수용 인원입니다.")
    private Integer dormitorySize; // 수용 인원

    @Schema(type = "List<DormitoryTermReq>", example = "dormitoryTermReqList", description= "거주 기간 별 가격 및 입.퇴사 날짜 리스트입니다.")
    private List<DormitoryTermReq> dormitoryTermReqList = new ArrayList<>();
}
