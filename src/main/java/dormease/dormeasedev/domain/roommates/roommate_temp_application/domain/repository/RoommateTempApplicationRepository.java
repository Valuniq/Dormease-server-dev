package dormease.dormeasedev.domain.roommates.roommate_temp_application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoommateTempApplicationRepository extends JpaRepository<RoommateTempApplication, Long> {

    Optional<RoommateTempApplication> findByCode(String code);

    Optional<RoommateTempApplication> findByRoommateMasterId(Long roommateMasterId);

    boolean existsByRoommateMasterId(Long roommateMasterId);
}
