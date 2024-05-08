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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private String title;

    @Lob
    private String content;

    // 부재 중 방문 동의 여부
    private Boolean isVisited;

    // 공개 여부
    private Boolean isPublic;

    @Enumerated(EnumType.STRING)
    private Progression progression;

    @Builder
    public Requestment(Long id, Resident resident, School school, String title, String content, Boolean isVisited, Boolean isPublic, Progression progression) {
        this.id = id;
        this.resident = resident;
        this.school = school;
        this.title = title;
        this.content = content;
        this.isVisited = isVisited;
        this.isPublic = isPublic;
        this.progression = progression;
    }
}
