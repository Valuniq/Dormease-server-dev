package dormease.dormeasedev.domain.notifications_requestments.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {

    ANNOUNCEMENT("ANNOUNCEMENT"), // 공지사항
    FAQ("FAQ"); // FAQ

    private String value;
}
