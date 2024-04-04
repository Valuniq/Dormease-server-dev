package dormease.dormeasedev.domain.dormitory_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_ticket_id")
    private MealTicket mealTicket;

    // 등본
    private String copy;

    // 우선 선발 증빙 서류
    private String prioritySelectionCopy;

    // 흡연 여부
    private Boolean isSmoking;

    // 비상 연락처
    private String emergencyContact;

    // 비상 연락처와의 관계
    private String emergencyRelation;

    // 은행명
    private String bankName;

    // 계좌번호
    private String accountNumber;

    // 신청 결과
    @Enumerated(EnumType.STRING)
    private DormitoryApplicationResult dormitoryApplicationResult;

    // 총액
    private Integer totalPrice;

    @Builder
    public DormitoryApplication(Long id, User user, Dormitory dormitory, MealTicket mealTicket, String copy, String prioritySelectionCopy, Boolean isSmoking, String emergencyContact, String emergencyRelation, String bankName, String accountNumber, DormitoryApplicationResult dormitoryApplicationResult, Integer totalPrice) {
        this.id = id;
        this.user = user;
        this.dormitory = dormitory;
        this.mealTicket = mealTicket;
        this.copy = copy;
        this.prioritySelectionCopy = prioritySelectionCopy;
        this.isSmoking = isSmoking;
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryApplicationResult = dormitoryApplicationResult;
        this.totalPrice = totalPrice;
    }
}
