package dormease.dormeasedev.domain.school.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class School extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private Long id;

    // 학교명
    private String name;

    @Builder
    public School(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
