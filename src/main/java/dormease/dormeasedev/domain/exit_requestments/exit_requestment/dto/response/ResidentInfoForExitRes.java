package dormease.dormeasedev.domain.exit_requestments.exit_requestment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ResidentInfoForExitRes {

    /** TODO :
     *   이름, 학번, 휴대전화, 학과, 학년 - User
     *   건물, 인실 - Dormitory
     *   호실 - Room
     *   침대번호 - Resident
     */

    @Schema(type = "Long", example = "1", description = "사생 ID")
    private Long residentId;

    @Schema(type = "String", example = "홍길동", description = "이름")
    private String residentName;

    @Schema(type = "String", example = "60240001", description = "학번")
    private String studentNumber;

    @Schema(type = "String", example = "010-1234-5678", description= "휴대전화")
    private String phoneNumber;

    @Schema(type = "String", example = "컴퓨터공학과", description = "학과")
    private String major;

    @Schema(type = "Integer", example = "1", description = "학년")
    private Integer schoolYear;

    @Schema(type = "String", example = "명덕관", description = "건물 (건물명)")
    private String dormitoryName;

    @Schema(type = "String", example = "4인실", description = "인실")
    private Integer roomSize;

    @Schema(type = "String", example = "999호", description = "호실")
    private Integer roomNumber;

    @Schema(type = "String", example = "1", description = "침대 번호")
    private Integer bedNumber;

}
