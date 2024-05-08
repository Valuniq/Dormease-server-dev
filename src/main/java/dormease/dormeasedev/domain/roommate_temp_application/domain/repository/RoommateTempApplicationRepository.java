package dormease.dormeasedev.domain.roommate_temp_application.domain.repository;

import dormease.dormeasedev.domain.roommate_temp_application.domain.RoommateTempApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoommateTempApplicationRepository extends JpaRepository<RoommateTempApplication, Long> {

    Optional<RoommateTempApplication> findByCode(String code);

    Optional<RoommateTempApplication> findByRoommateMasterId(Long roommateMasterId);

    boolean existsByRoommateMasterId(Long roommateMasterId);
}
