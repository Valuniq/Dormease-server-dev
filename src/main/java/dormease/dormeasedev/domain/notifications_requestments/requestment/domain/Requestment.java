package dormease.dormeasedev.domain.notifications_requestments.requestment.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.users.user.domain.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    // 부재 중 방문 동의 여부
    private Boolean consentDuringAbsence;

    // 공개 여부
    private Boolean visibility;

    @Enumerated(EnumType.STRING)
    private Progression progression;

    @Builder
    public Requestment(Long id, User user, String title, String content, Boolean consentDuringAbsence, Boolean visibility, Progression progression) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.consentDuringAbsence = consentDuringAbsence;
        this.visibility = visibility;
        this.progression = progression;
    }
}
