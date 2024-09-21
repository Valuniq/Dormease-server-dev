package dormease.dormeasedev.domain.school_settings.standard_setting.dto.response;

import dormease.dormeasedev.domain.school_settings.standard_setting.domain.FreshmanStandard;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.TiePriority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StandardSettingRes {

    @Schema(type = "double", example = "2.5", description = "최소 학점")
    private double minScore;

    @Schema(type = "int", example = "30", description = "성적 비율")
    private int scoreRatio;

    @Schema(type = "int", example = "70", description = "거리 비율")
    private int distanceRatio;

    @Schema(type = "boolean", example = "true", description = "상/벌점 반영 여부")
    private boolean pointReflection;

    @Schema(type = "TiePriority", example = "SCORE", description = "동점자 우선 순위 산정 방식. SCORE, DISTANCE 中 1.")
    private TiePriority tiePriority;

    @Schema(type = "FreshmanStandard", example = "LONG_DISTANCE", description = "신입생 점수 산정 방식. EVERYONE, LONG_DISTANCE 中 1.")
    private FreshmanStandard freshmanStandard;

    @Schema(type = "boolean", example = "true", description = "우선 선발 설정 활성화 여부")
    private boolean prioritySelection;

    @Schema(type = "boolean", example = "true", description = "이동 합격 설정 활성화 여부")
    private boolean movePassSelection;

    @Schema(type = "boolean", example = "true", description = "흡연 설정 활성화 여부")
    private boolean sameSmoke;

    @Schema(type = "boolean", example = "true", description = "동일 기간 설정 활성화 여부")
    private boolean sameTerm;

    @Schema(type = "boolean", example = "true", description = "입사 서약서")
    private String entrancePledge;

    @Schema(type = "List<DistanceScoreRes>", example = "distanceScoreReq", description = "거리 점수 리스트입니다.")
    private List<DistanceScoreRes> distanceScoreResList;
}
