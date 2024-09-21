package dormease.dormeasedev.domain.school_settings.standard_setting.service;

import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.DistanceScore;
import dormease.dormeasedev.domain.school_settings.distance_score.domain.repository.DistanceScoreRepository;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.school_settings.region.domain.repository.RegionRepository;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository.RegionDistanceScoreRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.repository.StandardSettingRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.CreateStandardSettingReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.request.DistanceScoreReq;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.response.DistanceScoreRes;
import dormease.dormeasedev.domain.school_settings.standard_setting.dto.response.StandardSettingRes;
import dormease.dormeasedev.domain.school_settings.standard_setting.exception.StandardSettingExistException;
import dormease.dormeasedev.domain.school_settings.standard_setting.exception.StandardSettingNotFoundException;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StandardSettingService {

    private final StandardSettingRepository standardSettingRepository;
    private final DistanceScoreRepository distanceScoreRepository;
    private final RegionRepository regionRepository;
    private final RegionDistanceScoreRepository regionDistanceScoreRepository;

    private final UserService userService;

    @Transactional
    public StandardSetting createStandardSetting(UserDetailsImpl userDetailsImpl, CreateStandardSettingReq createStandardSettingReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        if (standardSettingRepository.existsBySchool(school))
            throw new StandardSettingExistException();

        // 거리 기준 저장
        List<RegionDistanceScore> regionDistanceScoreList = new ArrayList<>();
        List<DistanceScoreReq> distanceScoreReqList = createStandardSettingReq.getDistanceScoreReqList();
        for (DistanceScoreReq distanceScoreReq : distanceScoreReqList) {
            DistanceScore distanceScore = distanceScoreRepository.findById(distanceScoreReq.getDistanceScoreId())
                    .orElseThrow(InvalidParameterException::new);
//            List<Long> regionIdList = distanceScoreReq.getRegionIdList();
            List<String> regionNameList = distanceScoreReq.getRegionNameList();

            for (String regionName : regionNameList) {
                Region region = regionRepository.findByFullName(regionName)
                        .orElseThrow(InvalidParameterException::new);

                RegionDistanceScore regionDistanceScore = RegionDistanceScore.builder()
                        .school(school)
                        .distanceScore(distanceScore)
                        .region(region)
                        .build();
                regionDistanceScoreList.add(regionDistanceScore);
            }
        }
        regionDistanceScoreRepository.saveAll(regionDistanceScoreList);

        return StandardSetting.builder()
                .school(school)
                .minScore(createStandardSettingReq.getMinScore())
                .scoreRatio(createStandardSettingReq.getScoreRatio())
                .distanceRatio(createStandardSettingReq.getDistanceRatio())
                .pointReflection(createStandardSettingReq.isPointReflection())
                .tiePriority(createStandardSettingReq.getTiePriority())
                .freshmanStandard(createStandardSettingReq.getFreshmanStandard())
                .prioritySelection(createStandardSettingReq.isPrioritySelection())
                .movePassSelection(createStandardSettingReq.isMovePassSelection())
                .sameSmoke(createStandardSettingReq.isSameSmoke())
                .sameTerm(createStandardSettingReq.isSameTerm())
                .entrancePledge(createStandardSettingReq.getEntrancePledge())
                .build();
    }

    public StandardSettingRes findStandardSetting(UserDetailsImpl userDetailsImpl, Long standardSettingId) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        StandardSetting standardSetting = standardSettingRepository.findById(standardSettingId)
                .orElseThrow(StandardSettingNotFoundException::new);
        if (!standardSetting.getSchool().equals(school))
            throw new IllegalArgumentException();

        List<DistanceScore> distanceScoreList = distanceScoreRepository.findAll();
        List<DistanceScoreRes> distanceScoreResList = new ArrayList<>();
        for (DistanceScore distanceScore : distanceScoreList) {
            List<RegionDistanceScore> regionDistanceScoreList = regionDistanceScoreRepository.findBySchoolAndDistanceScore(school, distanceScore);
            List<String> regionNameList = new ArrayList<>();
            for (RegionDistanceScore regionDistanceScore : regionDistanceScoreList) {
                Region region = regionDistanceScore.getRegion();
                regionNameList.add(region.getFullName());
            }
            DistanceScoreRes distanceScoreRes = DistanceScoreRes.builder()
                    .distanceScore(distanceScore.getScore())
                    .regionNameList(regionNameList)
                    .build();
            distanceScoreResList.add(distanceScoreRes);
        }

        return StandardSettingRes.builder()
                .minScore(standardSetting.getMinScore())
                .scoreRatio(standardSetting.getScoreRatio())
                .distanceRatio(standardSetting.getDistanceRatio())
                .pointReflection(standardSetting.isPointReflection())
                .tiePriority(standardSetting.getTiePriority())
                .freshmanStandard(standardSetting.getFreshmanStandard())
                .prioritySelection(standardSetting.isPrioritySelection())
                .movePassSelection(standardSetting.isMovePassSelection())
                .sameSmoke(standardSetting.isSameSmoke())
                .sameTerm(standardSetting.isSameTerm())
                .entrancePledge(standardSetting.getEntrancePledge())
                .distanceScoreResList(distanceScoreResList)
                .build();
    }
}
