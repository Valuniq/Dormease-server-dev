package dormease.dormeasedev.domain.notification.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String title;

    // 핀 여부
    private Boolean pinned;

    private String writer;

    @Builder
    public Notification(Long id, School school, NotificationType notificationType, String title, Boolean pinned, String writer) {
        this.id = id;
        this.school = school;
        this.notificationType = notificationType;
        this.title = title;
        this.pinned = pinned;
        this.writer = writer;
    }
}
