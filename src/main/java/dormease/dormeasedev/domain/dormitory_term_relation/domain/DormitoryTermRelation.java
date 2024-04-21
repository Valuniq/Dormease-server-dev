package dormease.dormeasedev.domain.dormitory_term_relation.domain;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Dormitory - DormitoryTerm 중간 테이블
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryTermRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_setting_term_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_term_id")
    private DormitoryTerm dormitoryTerm;

    @Builder
    public DormitoryTermRelation(Long id, Dormitory dormitory, DormitoryTerm dormitoryTerm) {
        this.id = id;
        this.dormitory = dormitory;
        this.dormitoryTerm = dormitoryTerm;
    }
}
