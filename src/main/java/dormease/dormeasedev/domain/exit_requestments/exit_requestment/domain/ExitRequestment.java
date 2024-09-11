package dormease.dormeasedev.domain.exit_requestments.exit_requestment.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.users.resident.domain.Resident;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class ExitRequestment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exit_requestment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    // 퇴실 날짜
    private LocalDate exitDate;

    // 열쇠 수령 여부
    private Boolean hasKey;

    // 열쇠 번호
    private String keyNumber;

    // 은행명
    private String bankName;

    // 계좌 번호
    private String accountNumber;

    // 보증금 환급 여부
    @Enumerated(EnumType.STRING)
    private SecurityDepositReturnStatus securityDepositReturnStatus;

    // update 함수
    public void updateSecurityDepositReturnStatus(SecurityDepositReturnStatus securityDepositReturnStatus) {
        this.securityDepositReturnStatus = securityDepositReturnStatus;
    }

    @Builder
    public ExitRequestment(Long id, Resident resident, LocalDate exitDate, Boolean hasKey, String keyNumber, String bankName, String accountNumber) {
        this.id = id;
        this.resident = resident;
        this.exitDate = exitDate;
        this.hasKey = hasKey;
        this.keyNumber = keyNumber;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.securityDepositReturnStatus = SecurityDepositReturnStatus.UNPAID; // 최초 미지급
    }
}
