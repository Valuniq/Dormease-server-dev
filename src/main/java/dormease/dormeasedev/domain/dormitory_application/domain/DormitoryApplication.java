package dormease.dormeasedev.domain.dormitory_application.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.term.domain.Term;
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
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dormitory_application_setting_id")
    private DormitoryApplicationSetting dormitoryApplicationSetting;

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

    private Boolean dormitoryPayment;

    // 신청 결과
    @Enumerated(EnumType.STRING)
    private DormitoryApplicationResult dormitoryApplicationResult;

    // 총액
    private Integer totalPrice;

    // 이전 or 현재 입사 신청 상태
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public void updateDormitoryApplicationResult(DormitoryApplicationResult dormitoryApplicationResult) {
        this.dormitoryApplicationResult = dormitoryApplicationResult;
    }

    public void updateCopy(String copy) {
        this.copy = copy;
    }

    public void updatePrioritySelectionCopy(String prioritySelectionCopy) {
        this.prioritySelectionCopy = prioritySelectionCopy;
    }

    // 사생 관리 - 사생 정보 수정 시 사용
    public void updateResidentPrivateInfo(String emergencyContact, String emergencyRelation, String bankName, String accountNumber, Boolean dormitoryPayment) {
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryPayment = dormitoryPayment;
    }

    @Builder
    public DormitoryApplication(User user, Term term, DormitoryApplicationSetting dormitoryApplicationSetting, Dormitory dormitory, MealTicket mealTicket, String copy, String prioritySelectionCopy, Boolean isSmoking, String emergencyContact, String emergencyRelation, String bankName, String accountNumber, Boolean dormitoryPayment, DormitoryApplicationResult dormitoryApplicationResult, Integer totalPrice, ApplicationStatus applicationStatus) {
        this.user = user;
        this.term = term;
        this.dormitoryApplicationSetting = dormitoryApplicationSetting;
        this.dormitory = dormitory;
        this.mealTicket = mealTicket;
        this.copy = copy;
        this.prioritySelectionCopy = prioritySelectionCopy;
        this.isSmoking = isSmoking;
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryPayment = dormitoryPayment;
        this.dormitoryApplicationResult = dormitoryApplicationResult;
        this.totalPrice = totalPrice;
        this.applicationStatus = applicationStatus;
    }
}
