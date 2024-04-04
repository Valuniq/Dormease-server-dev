package dormease.dormeasedev.domain.roommate_temp_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.resident.domain.Resident;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RoommateTempApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roommate_temp_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    private String code;

    // 룸메이트 신청 여부 - 방장이 버튼 클릭 시 true
    private Boolean isApplied;

    @Builder
    public RoommateTempApplication(Long id, Resident resident, String code, Boolean isApplied) {
        this.id = id;
        this.resident = resident;
        this.code = code;
        this.isApplied = isApplied;
    }
}
