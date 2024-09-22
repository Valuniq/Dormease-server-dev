package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationStatus {

    BEFORE("BEFORE"), // 이전
    NOW("NOW"),  // 현재 / 사생 선발 (합/불) 검사 돌리면 BEFORE
    READY("READY"), // 시작 전 (준비 중)
    DEPOSIT("DEPOSIT"), // 입금 처리 중
    PASS("PASS") // 합격자 처리 중 (방 배정 전)
    ;

    // READY      -->      NOW      -->       DEPOSIT       -->       PASS       -->       BEFORE
    //       신청 기간 시작        합/불 검사                입금 처리               방 배정
    //                                      (입사 신청자들의 입사 신청에서 입금 여부 확인)
    private String value;
}
