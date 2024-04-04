package dormease.dormeasedev.domain.block.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.notification.domain.Notification;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Block extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private String imageUrl;

    private Integer sequence;

    @Lob
    private String content;

    @Builder
    public Block(Long id, Notification notification, String imageUrl, Integer sequence, String content) {
        this.id = id;
        this.notification = notification;
        this.imageUrl = imageUrl;
        this.sequence = sequence;
        this.content = content;
    }
}
