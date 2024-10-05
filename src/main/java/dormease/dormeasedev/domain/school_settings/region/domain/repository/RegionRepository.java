package dormease.dormeasedev.domain.school_settings.region.domain.repository;

import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByFullName(String regionName);

    List<Region> findAllByParentRegion(Region region);

    List<Region> findByParentRegionIsNull();
}
