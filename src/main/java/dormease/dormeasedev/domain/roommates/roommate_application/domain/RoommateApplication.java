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

    @Enumerated(EnumType.STRING)
    private RoommateApplicationResult roommateApplicationResult;

    @Builder
    public RoommateApplication(Long id, RoommateApplicationResult roommateApplicationResult) {
        this.id = id;
        this.roommateApplicationResult = roommateApplicationResult;
    }
}
