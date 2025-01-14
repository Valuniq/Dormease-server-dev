package dormease.dormeasedev.domain.exit_requestments.refund_requestment.domain;

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
public class RefundRequestment extends BaseEntity {
    
    // TODO : 퇴실 날짜 - 시간은 필요 없을지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_requestment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id")
    private Resident resident;

    // 퇴실 날짜
    private LocalDate exitDate;

    // 은행명
    private String bankName;

    // 계좌 번호
    private String accountNumber;

    @Builder
    public RefundRequestment(Resident resident, LocalDate exitDate, String bankName, String accountNumber) {
        this.resident = resident;
        this.exitDate = exitDate;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}
