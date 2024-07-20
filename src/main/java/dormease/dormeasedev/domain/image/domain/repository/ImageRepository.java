package dormease.dormeasedev.domain.image.domain.repository;

import dormease.dormeasedev.domain.image.domain.Image;
import dormease.dormeasedev.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByNotification(Notification notification);
}