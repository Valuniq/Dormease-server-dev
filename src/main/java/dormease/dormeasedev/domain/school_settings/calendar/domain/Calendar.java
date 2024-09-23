package dormease.dormeasedev.domain.school_settings.calendar.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.school.domain.School;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
public class Calendar extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    private LocalDate startDate;

    private LocalDate endDate;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Builder
    public Calendar(Long id, School school, LocalDate startDate, LocalDate endDate, String title, String content, Color color) {
        this.id = id;
        this.school = school;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.color = color;
    }

}
