package dormease.dormeasedev.domain.roommates.roommate_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RoommateApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roommate_application_id")
    private Long id;

    private String code;

    // 룸메이트 신청 여부 - 방장이 버튼 클릭 시 true
    private boolean isApplied;

    // 방장 id (방장의 resident id)
    private Long roommateMasterId;

    public void updateApplied(boolean applied) {
        this.isApplied = applied;
    }

    @Builder
    public RoommateApplication(String code, Long roommateMasterId) {
        this.code = code;
        this.isApplied = false;
        this.roommateMasterId = roommateMasterId;
    }
}
