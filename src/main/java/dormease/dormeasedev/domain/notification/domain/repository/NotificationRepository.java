package dormease.dormeasedev.domain.notification.domain.repository;

import dormease.dormeasedev.domain.notification.domain.Notification;
import dormease.dormeasedev.domain.notification.domain.NotificationType;
import dormease.dormeasedev.domain.school.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllBySchoolAndNotificationType(School school, NotificationType notificationType);

    Page<Notification> findNotificationsBySchoolAndNotificationType(School school, NotificationType notificationType, Pageable pageable);

    Optional<Notification> findTopBySchoolAndNotificationTypeAndPinnedOrderByCreatedDateDesc(School school, NotificationType notificationType, Boolean pinned);
}
