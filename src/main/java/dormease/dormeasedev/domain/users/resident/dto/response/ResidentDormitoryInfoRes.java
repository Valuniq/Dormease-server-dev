package dormease.dormeasedev.domain.users.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResidentDormitoryInfoRes {

    // 건물 id
    @Schema(type = "Long", example = "1", description = "기숙사 ID")
    private Long dormitoryId;

    @Schema(type = "String", example = "건물명", description= "사생이 거주하는 건물명입니다.")
    private String dormitoryName;

    @Schema(type = "Integer", example = "2", description= "사생이 거주하는 건물의 인실입니다.")
    private Integer roomSize;

    @Schema(type = "Integer", example = "1", description= "사생이 거주하는 호실의 번호입니다.")
    private Integer roomNumber;

    @Schema(type = "Integer", example = "1", description= "사생의 호실 침대번호 입니다.")
    private Integer bedNumber;

    @Schema(type = "Long", example = "1", description = "거주기간 ID")
    private Long termId;

    @Schema(type = "String", example = "학기", description= "사생의 거주기간입니다.")
    private String termName;

    @Schema(type = "Boolean", example = "true", description= "사생의 룸메이트 신청 여부입니다.")
    private Boolean isApplyRoommate;

    @Schema(type = "List", example = "김사생, 이사생, 최사생", description= "같은 호실에 거주하는 사생의 이름 리스트입니다.")
    private List<String> roommateNames;

    @Builder
    public ResidentDormitoryInfoRes(Long dormitoryId, String dormitoryName, Integer roomSize, Integer roomNumber, Integer bedNumber, Long termId, String termName, Boolean isApplyRoommate, List<String> roommateNames) {
        this.dormitoryId = dormitoryId;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        this.termId = termId;
        this.termName = termName;
        this.isApplyRoommate = isApplyRoommate;
        this.roommateNames = roommateNames;
    }
}
