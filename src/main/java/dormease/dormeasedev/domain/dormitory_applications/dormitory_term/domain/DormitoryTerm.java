package dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기숙사 - 방타입 중간테이블과 거주기간의 연관관계가 M:M이므로 그에 대한 중간 테이블
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_term_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_room_type_id")
    private DormitoryRoomType dormitoryRoomType;

    private Integer price;

    @Builder
    public DormitoryTerm(Term term, DormitoryRoomType dormitoryRoomType, Integer price) {
        this.term = term;
        this.dormitoryRoomType = dormitoryRoomType;
        this.price = price;
    }
}
