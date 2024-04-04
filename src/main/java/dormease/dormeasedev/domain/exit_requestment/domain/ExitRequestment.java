package dormease.dormeasedev.domain.exit_requestment.domain;

import dormease.dormeasedev.domain.common.BaseEntity;
import dormease.dormeasedev.domain.resident.domain.Resident;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    // 퇴실 날짜
    private LocalDate exitDate;

    // 열쇠 수령 여부
    private Boolean hasKey;

    // 열쇠 번호
    private String ketNumber;

    // 은행명
    private String bankName;

    // 계좌 번호
    private String accountNumber;

    // 보증금 환급 여부
    private Boolean isReturnSecurityDeposit;

    @Builder
    public ExitRequestment(Long id, Resident resident, LocalDate exitDate, Boolean hasKey, String ketNumber, String bankName, String accountNumber, Boolean isReturnSecurityDeposit) {
        this.id = id;
        this.resident = resident;
        this.exitDate = exitDate;
        this.hasKey = hasKey;
        this.ketNumber = ketNumber;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.isReturnSecurityDeposit = isReturnSecurityDeposit;
    }
}
