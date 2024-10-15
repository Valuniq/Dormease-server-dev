package dormease.dormeasedev.domain.users.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AvailableDormitoryApplicationSettingRes {

    @Schema(type = "Long", example = "1", description = "입사신청설정의 id입니다.")
    private Long dormitoryApplicationSettingId;

    @Schema(type = "String", example = "2024년 제1차 명지대학교 인문생활관 입사 신청", description = "입사신청설정의 제목입니다.")
    private String title;

    @Schema(type = "List<Available>", example = "Schemas의 AvailableTermRes를 확인해주세요", description = "입사신청설정에 따른 거주기간의 목록입니다.")
    private List<AvailableTermRes> availableTermRes;

    @Builder
    public AvailableDormitoryApplicationSettingRes(Long dormitoryApplicationSettingId, String title, List<AvailableTermRes> availableTermRes) {
        this.dormitoryApplicationSettingId = dormitoryApplicationSettingId;
        this.title = title;
        this.availableTermRes = availableTermRes;
    }
}
