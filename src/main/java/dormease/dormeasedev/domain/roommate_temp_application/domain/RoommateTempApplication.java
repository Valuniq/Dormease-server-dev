package dormease.dormeasedev.domain.roommate_temp_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.resident.domain.Resident;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RoommateTempApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roommate_temp_application_id")
    private Long id;

    private String code;

    // 룸메이트 신청 여부 - 방장이 버튼 클릭 시 true
    private Boolean isApplied;

    // 방장 id (방장의 resident id)
    private Long roommateMasterId;

    @OneToMany(mappedBy = "roommateTempApplication")
    private List<Resident> residents = new ArrayList<>();

    // update 함수
    public void updateIsApplied() {
        this.isApplied = true;
    }

    @Builder
    public RoommateTempApplication(Long id, String code, Boolean isApplied, Long roommateMasterId, List<Resident> residents) {
        this.id = id;
        this.code = code;
        this.isApplied = isApplied;
        this.roommateMasterId = roommateMasterId;
        this.residents = residents;
    }
}
