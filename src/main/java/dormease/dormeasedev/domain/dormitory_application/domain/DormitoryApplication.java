package dormease.dormeasedev.domain.dormitory_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_term.domain.DormitoryTerm;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormiroty_term_id")
    private DormitoryTerm dormitoryTerm;

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

    // 이전 or 현재 입사 신청 상태
    private ApplicationStatus applicationStatus;

    @Builder
    public DormitoryApplication(Long id, User user, DormitoryTerm dormitoryTerm, String copy, String prioritySelectionCopy, Boolean isSmoking, String emergencyContact, String emergencyRelation, String bankName, String accountNumber, DormitoryApplicationResult dormitoryApplicationResult, Integer totalPrice, ApplicationStatus applicationStatus) {
        this.id = id;
        this.user = user;
        this.dormitoryTerm = dormitoryTerm;
        this.copy = copy;
        this.prioritySelectionCopy = prioritySelectionCopy;
        this.isSmoking = isSmoking;
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryApplicationResult = dormitoryApplicationResult;
        this.totalPrice = totalPrice;
        this.applicationStatus = applicationStatus;
    }
}
