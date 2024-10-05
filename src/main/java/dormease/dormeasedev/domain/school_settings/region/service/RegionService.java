package dormease.dormeasedev.domain.school_settings.region.service;

import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.school_settings.region.domain.repository.RegionRepository;
import dormease.dormeasedev.domain.school_settings.region.dto.RegionRes;
import dormease.dormeasedev.domain.school_settings.region.exception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionRes> findBigRegions() {
        return regionRepository.findByParentRegionIsNull().stream()
                .map(region -> RegionRes.builder()
                        .regionId(region.getId())
                        .regionName(region.getFullName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<RegionRes> findSmallRegions(Long regionId) {
        Region parentRegion = regionRepository.findById(regionId)
                .orElseThrow(RegionNotFoundException::new);
        return regionRepository.findAllByParentRegion(parentRegion).stream()
                .map(region -> RegionRes.builder()
                        .regionId(region.getId())
                        .regionName(region.getSingleName())
                        .build())
                .collect(Collectors.toList());
    }
}
