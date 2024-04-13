package dormease.dormeasedev.domain.dormitory_term.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryTerm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_term_id")
    private Long id;

    // 거주 기간
    private String term;

    private Integer price;

    // 입사-퇴사 기간 여기 넣을지?

    // 이전 내역인지 구분하는 컬럼

    @Builder
    public DormitoryTerm(Long id, String term, Integer price) {
        this.id = id;
        this.term = term;
        this.price = price;
    }
}
