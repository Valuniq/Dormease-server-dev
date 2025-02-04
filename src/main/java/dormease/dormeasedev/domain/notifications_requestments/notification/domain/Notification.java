package dormease.dormeasedev.domain.notifications_requestments.notification.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.notifications_requestments.file.domain.File;
import dormease.dormeasedev.domain.notifications_requestments.image.domain.Image;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String writer;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String title;

    // 핀 여부
    private Boolean pinned;

    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE)
    List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE)
    List<File> files = new ArrayList<>();

    @Builder
    public Notification(School school, String writer, NotificationType notificationType, String title, Boolean pinned, String content) {
        this.school = school;
        this.writer = writer;
        this.notificationType = notificationType;
        this.title = title;
        this.pinned = pinned;
        this.content = content;
    }

    public void updateImages() {
        this.images = new ArrayList<>();
    }

    public void updateFiles() {
        this.files = new ArrayList<>();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePinned() {
        this.pinned = !this.pinned;
    }

}
