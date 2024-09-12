package dormease.dormeasedev.domain.notifications_requestments.requestment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Progression {

    // 검토중 진행중 답변완료
    IN_REVIEW("IN_REVIEW"), // 검토 중
    IN_PROGRESS("IN_PROGRESS"), // 진행 중
    ANSWER_COMPLETED("ANSWER_COMPLETED"); // 답변 완료

    private String value;
}
