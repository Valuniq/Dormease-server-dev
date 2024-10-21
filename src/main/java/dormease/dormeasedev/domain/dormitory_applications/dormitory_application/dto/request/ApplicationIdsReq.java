package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationIdsReq {

    @Schema(type = "List<Long>", example = "[1, 2, 5]", description= "합/불 검사를 진행할 입사 신청 ID 목록을 입력해주세요.")
    private List<Long> dormitoryApplicationIds;
}
