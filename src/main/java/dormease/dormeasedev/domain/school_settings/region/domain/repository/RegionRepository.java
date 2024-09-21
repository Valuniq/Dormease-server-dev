package dormease.dormeasedev.domain.school_settings.region.domain.repository;

import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
