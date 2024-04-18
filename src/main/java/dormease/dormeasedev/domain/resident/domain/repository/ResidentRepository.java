package dormease.dormeasedev.domain.resident.domain.repository;

import dormease.dormeasedev.domain.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.resident.domain.Resident;
import dormease.dormeasedev.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByDormitorySettingTerm(DormitorySettingTerm settingTerm);

    List<Resident> findByRoom(Room room);

    List<Resident> findByDormitorySettingTermAndRoom(DormitorySettingTerm dormitorySettingTerm, Room room);
}
