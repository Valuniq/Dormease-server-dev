package dormease.dormeasedev.domain.image.domain;

import dormease.dormeasedev.domain.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private String imageUrl;

    public void addNotification(Notification notification) {
        this.notification = notification;
        if (notification.getImages() == null)
            notification.updateImages();
        notification.getImages().add(this);
    }

    @Builder
    public Image(Notification notification, String imageUrl) {
        this.notification = notification;
        this.imageUrl = imageUrl;
    }
}
