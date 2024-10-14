package dormease.dormeasedev.domain.school_settings.standard_setting.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.repository.DistanceScoreRepository;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.school_settings.region.domain.repository.RegionRepository;
import dormease.dormeasedev.domain.school_settings.region.dto.RegionRes;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository.RegionDistanceScoreRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.repository.StandardSettingRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.DistanceScoreReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.ModifyStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.response.DistanceScoreRes;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.response.StandardSettingRes;
import dormease.dormeasedev.domain.school_settings.standard_setting.exception.StandardSettingExistException;
import dormease.dormeasedev.domain.school_settings.standard_setting.exception.StandardSettingNotFoundException;
import dormease.dormeasedev.domain.school_settings.standard_setting.mapper.StandardSettingMapper;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StandardSettingService {

    private final StandardSettingRepository standardSettingRepository;
    private final DistanceScoreRepository distanceScoreRepository;
    private final RegionRepository regionRepository;
    private final RegionDistanceScoreRepository regionDistanceScoreRepository;
    private final StandardSettingMapper standardSettingMapper;

    private final UserService userService;

    @Transactional
    public StandardSetting createStandardSetting(UserDetailsImpl userDetailsImpl, CreateStandardSettingReq createStandardSettingReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        if (standardSettingRepository.existsBySchool(school))
            throw new StandardSettingExistException();

        // 거리 기준 저장
        List<RegionDistanceScore> regionDistanceScoreList = createStandardSettingReq.getDistanceScoreReqList().stream()
                .map(distanceScoreReq -> {
                    DistanceScore distanceScore = distanceScoreRepository.findById(distanceScoreReq.getDistanceScoreId())
                            .orElseThrow(InvalidParameterException::new);

                    return distanceScoreReq.getRegionIdList().stream()
                            .map(regionId -> regionRepository.findById(regionId)
                                    .orElseThrow(InvalidParameterException::new))
                            .map(region -> RegionDistanceScore.builder()
                                    .school(school)
                                    .distanceScore(distanceScore)
                                    .region(region)
                                    .build())
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        regionDistanceScoreRepository.saveAll(regionDistanceScoreList);

        StandardSetting standardSetting = standardSettingMapper.createStandardSettingReqToStandardSetting(school, createStandardSettingReq);
        return standardSettingRepository.save(standardSetting);
    }

    public StandardSettingRes findStandardSetting(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        StandardSetting standardSetting = standardSettingRepository.findBySchool(school)
                .orElseThrow(StandardSettingNotFoundException::new);
        if (!standardSetting.getSchool().equals(school))
            throw new IllegalArgumentException();

        List<DistanceScore> distanceScoreList = distanceScoreRepository.findAll();
        List<DistanceScoreRes> distanceScoreResList = distanceScoreList.stream()
                .map(distanceScore -> {
                    List<RegionDistanceScore> regionDistanceScoreList = regionDistanceScoreRepository.findBySchoolAndDistanceScore(school, distanceScore);

                    List<RegionRes> regionResList = regionDistanceScoreList.stream()
                            .map(regionDistanceScore -> {
                                Region region = regionDistanceScore.getRegion();
                                return RegionRes.builder()
                                        .regionId(region.getId())
                                        .regionName(region.getSingleName())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return DistanceScoreRes.builder()
                            .distanceScoreId(distanceScore.getId())
                            .distanceScore(distanceScore.getScore())
                            .regionResList(regionResList)
                            .build();
                })
                .collect(Collectors.toList());

        return standardSettingMapper.toStandardSettingRes(standardSetting, distanceScoreResList);
    }

    @Transactional
    public void modifyStandardSetting(UserDetailsImpl userDetailsImpl, Long standardSettingId, ModifyStandardSettingReq modifyStandardSettingReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        StandardSetting standardSetting = standardSettingRepository.findById(standardSettingId)
                .orElseThrow(StandardSettingNotFoundException::new);
        if (!standardSetting.getSchool().equals(school))
            throw new IllegalArgumentException();

        // 기존 RegionDistanceScore 목록 조회
        List<RegionDistanceScore> existingRegionDistanceScores = regionDistanceScoreRepository.findBySchool(school);

        // 새로 들어온 DistanceScoreReqList와 기존 데이터를 비교
        List<RegionDistanceScore> updatedRegionDistanceScores = modifyStandardSettingReq.getDistanceScoreReqList().stream()
                .map(distanceScoreReq -> {
                    DistanceScore distanceScore = distanceScoreRepository.findById(distanceScoreReq.getDistanceScoreId())
                            .orElseThrow(InvalidParameterException::new);

                    return distanceScoreReq.getRegionIdList().stream()
                            .map(regionId -> regionRepository.findById(regionId)
                                    .orElseThrow(InvalidParameterException::new))
                            .map(region -> {
                                // 기존 데이터가 있으면 수정, 없으면 새로운 데이터 생성
                                return existingRegionDistanceScores.stream()
                                        .filter(existing -> existing.getDistanceScore().equals(distanceScore) &&
                                                existing.getRegion().equals(region))
                                        .findFirst()
                                        .orElseGet(() -> RegionDistanceScore.builder()
                                                .school(school)
                                                .distanceScore(distanceScore)
                                                .region(region)
                                                .build());
                            })
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 기존 데이터에서 삭제할 항목 제거
        List<RegionDistanceScore> toRemove = existingRegionDistanceScores.stream()
                .filter(existing -> updatedRegionDistanceScores.stream()
                        .noneMatch(updated -> updated.getDistanceScore().equals(existing.getDistanceScore()) &&
                                updated.getRegion().equals(existing.getRegion())))
                .collect(Collectors.toList());

        // 삭제할 항목 제거
        regionDistanceScoreRepository.deleteAll(toRemove);

        // 변경된 데이터 저장 (추가 및 수정)
        regionDistanceScoreRepository.saveAll(updatedRegionDistanceScores);

        standardSettingMapper.updateStandardSettingFromDto(modifyStandardSettingReq, standardSetting);
    }
}
