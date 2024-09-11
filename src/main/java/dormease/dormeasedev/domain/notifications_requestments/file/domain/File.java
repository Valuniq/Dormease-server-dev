package dormease.dormeasedev.domain.notifications_requestments.file.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.notifications_requestments.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    // 파일 경로
    private String fileUrl;

    private String originalFileName;

    @Builder
    public File(Long id, Notification notification, String fileUrl, String originalFileName) {
        this.id = id;
        this.notification = notification;
        this.fileUrl = fileUrl;
        this.originalFileName = originalFileName;
    }

    public void addNotification(Notification notification) {
        this.notification = notification;
        if (notification.getFiles() == null)
            notification.updateFiles();
        notification.getFiles().add(this);
    }
}
