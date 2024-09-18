package dormease.dormeasedev.domain.notifications_requestments.requestment.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.users.student.domain.Student;
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
    @JoinColumn(name = "student_id")
    private Student student;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    // 부재 중 방문 동의 여부
    private Boolean consentDuringAbsence;

    // 공개 여부
    private Boolean visibility;

    @Enumerated(EnumType.STRING)
    private Progression progression;

    public void updateProgression(Progression progression) {
        this.progression = progression;
    }

    @Builder
    public Requestment(Student student, String title, String content, Boolean consentDuringAbsence, Boolean visibility) {
        this.student = student;
        this.title = title;
        this.content = content;
        this.consentDuringAbsence = consentDuringAbsence;
        this.visibility = visibility;
        this.progression = Progression.IN_REVIEW;
    }
}
