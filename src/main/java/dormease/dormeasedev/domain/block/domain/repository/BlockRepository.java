package dormease.dormeasedev.domain.block.domain.repository;

import dormease.dormeasedev.domain.block.domain.Block;
import dormease.dormeasedev.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    List<Block> findByNotification(Notification notification);
}
