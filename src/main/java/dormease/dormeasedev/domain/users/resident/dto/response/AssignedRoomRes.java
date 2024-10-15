package dormease.dormeasedev.domain.users.resident.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AssignedRoomRes {

    @Schema(type = "Integer", example = "1", description= "사생의 호실 배정 가능 여부입니다.")
    private boolean possible;

    @Schema(type = "Integer", example = "1", description= "사생의 호실 침대번호 입니다.")
    private Integer bedNumber;

    @Schema(type = "List", example = "김사생, 이사생, 최사생", description= "같은 호실에 거주하는 사생의 이름 리스트입니다.")
    private List<String> roommateNames;

    @Builder
    public AssignedRoomRes(boolean possible, Integer bedNumber, List<String> roommateNames) {
        this.possible = possible;
        this.bedNumber = bedNumber;
        this.roommateNames = roommateNames;
    }
}
