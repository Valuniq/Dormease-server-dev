package dormease.dormeasedev.domain.resident.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResidentPrivateInfoRes {

    @Schema(type = "String", example = "사용자", description= "사생의 이름입니다.")
    private String name;

    @Schema(type = "String", example = "60xxxxxx", description= "사생의 학번(또는 수험번호)입니다.")
    private String studentNumber;

    @Schema(type = "String", example = "컴퓨터공학과", description= "사생의 학과입니다.")
    private String major;

    // 학적
    @Schema(type = "String", example = "재학", description= "사생의 학적 상태입니다.")
    private String schoolStatus;

    // 거주지
    @Schema(type = "String", example = "서울특별시 서대문구 ...", description= "사생의 주소입니다.")
    private String address;

    // 학년
    @Schema(type = "Integer", example = "4", description= "사생의 학년입니다.")
    private Integer schoolYear;

    @Schema(type = "String", example = "전화번호", description= "사생의 전화번호입니다.")
    private String phoneNumber;

    @Schema(type = "String", example = "MALE / FEMALE", description= "사생의 성별입니다.")
    private String gender;

    @Schema(type = "Integer", example = "1", description= "사생의 상점 총점입니다.")
    private Integer bonusPoint;

    @Schema(type = "Integer", example = "1", description= "사생의 벌점 총점입니다.")
    private Integer minusPoint;

    @Schema(type = "Integer", example = "50", description= "사생이 신청한 식권의 식수입니다.")
    private Integer mealTicketCount;

    // 등본
    @Schema(type = "String", example = "https://dormease-s3-bucket.s3.amazonaws.com/13b1ab40-9c93-4833-9942...", description= "사생의 주민등록등본입니다.")
    private String copy;

    // 우선 선발 증빙 서류
    @Schema(type = "String", example = "https://dormease-s3-bucket.s3.amazonaws.com/13b1ab40-9c93-4833-9942...", description= "사생의 우선 선발 증빙서류입니다.")
    private String prioritySelectionCopy;

    // 흡연 여부
    @Schema(type = "Boolean", example = "true", description= "사생의 흡연여부입니다.")
    private Boolean isSmoking;

    // 비상 연락처
    @Schema(type = "String", example = "010xxxxxxxx", description= "사생의 비상연락처입니다.")
    private String emergencyContact;

    // 비상 연락처와의 관계
    @Schema(type = "String", example = "부", description= "사생의 비상연락처와의 관계입니다.")
    private String emergencyRelation;

    // 은행명
    @Schema(type = "String", example = "신한", description= "사생의 계좌 은행명입니다.")
    private String bankName;

    // 계좌번호
    @Schema(type = "String", example = "109512054", description= "사생의 계좌번호입니다.")
    private String accountNumber;

    // 생활관비 납부 여부
    @Schema(type = "Boolean", example = "true", description= "사생의 생활관비 납부 여부입니다.")
    private Boolean dormitoryPayment;

    // 열쇠 수령 여부
    @Schema(type = "Boolean", example = "true", description= "사생의 열쇠 수령 여부입니다.")
    private Boolean hasKey;

    // 개인정보동의 제3자제공동의 -> 회원일 경우? 무조건 true
    // 개인정보동의
    @Schema(type = "Boolean", example = "true", description= "사생의 개인정보 동의 여부입니다.")
    private Boolean personalInfoConsent;
    // 제3자정보제공동의
    @Schema(type = "Boolean", example = "true", description= "사생의 제3자 정보제공 동의 여부입니다.")
    private Boolean thirdPartyConsent;

    @Builder
    public ResidentPrivateInfoRes(String name, String studentNumber, String major, String schoolStatus, String address, Integer schoolYear,
                                  String phoneNumber, String gender, Integer bonusPoint, Integer minusPoint, Integer mealTicketCount, String copy,
                                  String prioritySelectionCopy, Boolean isSmoking, String emergencyContact, String emergencyRelation, String bankName,
                                  String accountNumber, Boolean dormitoryPayment, Boolean hasKey, Boolean personalInfoConsent, Boolean thirdPartyConsent) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.major = major;
        this.schoolStatus = schoolStatus;
        this.address = address;
        this.schoolYear = schoolYear;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.bonusPoint = bonusPoint;
        this.minusPoint = minusPoint;
        this.mealTicketCount = mealTicketCount;
        this.copy =copy;
        this.prioritySelectionCopy = prioritySelectionCopy;
        this.isSmoking = isSmoking;
        this.emergencyContact = emergencyContact;
        this.emergencyRelation = emergencyRelation;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.dormitoryPayment = dormitoryPayment;
        this.hasKey = hasKey;
        this.personalInfoConsent = personalInfoConsent;
        this.thirdPartyConsent = thirdPartyConsent;
    }
}
