package dormease.dormeasedev.domain.requestment.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Requestment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    private String title;

    @Lob
    private String content;

    // 부재 중 방문 동의 여부
    private Boolean consentDuringAbsence;

    // 공개 여부
    private Boolean visibility;

    @Enumerated(EnumType.STRING)
    private Progression progression;

    @Builder
    public Requestment(Long id, Resident resident, String title, String content, Boolean consentDuringAbsence, Boolean visibility, Progression progression) {
        this.id = id;
        this.resident = resident;
        this.title = title;
        this.content = content;
        this.consentDuringAbsence = consentDuringAbsence;
        this.visibility = visibility;
        this.progression = progression;
    }
}
