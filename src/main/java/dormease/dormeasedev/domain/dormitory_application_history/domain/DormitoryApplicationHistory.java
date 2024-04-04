package dormease.dormeasedev.domain.dormitory_application_history.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DormitoryApplicationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_application_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    // 기숙사명
    private String dormitoryName;

    // 인실
    private Integer roomSize;

    // 식권 개수
    private Integer mealTicketCount;

    // 보증금
    private Integer securityDeposit;

    // 기숙사비 + 식권 가격
    private Integer price;

    // 입사 서약서 동의 여부
    private Boolean isAgreeDormitory;

    // 학교명
    private String schoolName;

    @Builder
    public DormitoryApplicationHistory(Long id, User user, String copy, String prioritySelectionCopy, Boolean isSmoking, String emergencyContact, String emergencyRelation, String bankName, String accountNumber, DormitoryApplicationResult dormitoryApplicationResult, Integer totalPrice, String dormitoryName, Integer roomSize, Integer mealTicketCount, Integer securityDeposit, Integer price, Boolean isAgreeDormitory, String schoolName) {
        this.id = id;
        this.user = user;
        this.copy = copy;
        this.prioritySelectionCopy = prioritySelectionCopy;
        this.isSmoking = isSmoking;
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryApplicationResult = dormitoryApplicationResult;
        this.totalPrice = totalPrice;
        this.dormitoryName = dormitoryName;
        this.roomSize = roomSize;
        this.mealTicketCount = mealTicketCount;
        this.securityDeposit = securityDeposit;
        this.price = price;
        this.isAgreeDormitory = isAgreeDormitory;
        this.schoolName = schoolName;
    }
}
