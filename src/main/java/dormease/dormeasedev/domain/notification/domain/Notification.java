package dormease.dormeasedev.domain.notification.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.image.domain.Image;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.user.domain.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    public Notification(School school, User user, NotificationType notificationType, String title, Boolean pinned, String content) {
        this.school = school;
        this.user = user;
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

    public void updatePinned() {
        this.pinned = !this.pinned;
    }

}
