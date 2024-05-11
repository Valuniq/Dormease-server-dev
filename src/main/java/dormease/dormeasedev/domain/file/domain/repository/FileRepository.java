package dormease.dormeasedev.domain.file.domain.repository;

import dormease.dormeasedev.domain.file.domain.File;
import dormease.dormeasedev.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    boolean existsByNotification(Notification notification);
}
