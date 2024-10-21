package dormease.dormeasedev.domain.dormitory_applications.dormitory_application.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplication;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.DormitoryApplicationResult;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.domain.repository.DormitoryApplicationRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ApplicationIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ApplicationResultIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.request.ModifyApplicationResultIdsReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.ApplicantListRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.DormitoryApplicationWebRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application.dto.response.InspectDormitoryApplicationRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.school_settings.region.domain.Region;
import dormease.dormeasedev.domain.school_settings.region.domain.repository.RegionRepository;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.RegionDistanceScore;
import dormease.dormeasedev.domain.school_settings.region_distance_score.domain.repository.RegionDistanceScoreRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.FreshmanStandard;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.StandardSetting;
import dormease.dormeasedev.domain.school_settings.standard_setting.domain.repository.StandardSettingRepository;
import dormease.dormeasedev.domain.school_settings.standard_setting.exception.StandardSettingNotFoundException;
import dormease.dormeasedev.domain.users.student.domain.Student;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.exception.InvalidSchoolAuthorityException;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationWebService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final DormitoryApplicationRepository dormitoryApplicationRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final StandardSettingRepository standardSettingRepository;
    private final RegionDistanceScoreRepository regionDistanceScoreRepository;
    private final RegionRepository regionRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;

    private final UserService userService;

    // TODO : N+1 최적화
    public ApplicantListRes findDormitoryApplications(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findBySchoolAndApplicationStatus(school, ApplicationStatus.NOW)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryApplicationWebRes> dormitoryApplicationWebResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            User user = student.getUser();

            DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
            DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
            Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
            RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(applicationDormitory.getName())
                    .roomSize(applicationRoomType.getRoomSize())
                    .build();

            DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(user.getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
                    .address(student.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryRoomTypeRes(null)
                    .dormitoryApplicationResult(DormitoryApplicationResult.WAIT)
                    .build();

            dormitoryApplicationWebResList.add(dormitoryApplicationWebRes);
        }
        return ApplicantListRes.builder()
                .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                .dormitoryApplicationWebResList(dormitoryApplicationWebResList)
                .build();
    }

    public List<DormitoryApplicationWebRes> searchDormitoryApplications(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId, String searchWord) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        List<DormitoryApplication> dormitoryApplicationList =
                dormitoryApplicationRepository.findAllByDormitoryApplicationSettingAndStudent_StudentNumberContainingOrStudent_User_NameContaining(dormitoryApplicationSetting, searchWord, searchWord);

        List<DormitoryApplicationWebRes> dormitoryApplicationWebResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            User user = student.getUser();

            DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
            DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
            Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
            RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(applicationDormitory.getName())
                    .roomSize(applicationRoomType.getRoomSize())
                    .build();

            DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes;
            if (dormitoryApplication.getDormitoryApplicationResult().equals(DormitoryApplicationResult.NON_PASS) || dormitoryApplication.getDormitoryApplicationResult().equals(DormitoryApplicationResult.WAIT))
                resultDormitoryRoomTypeRes = null;
            else {
                DormitoryTerm resultDormitoryTerm = dormitoryApplication.getResultDormitoryTerm();
                DormitoryRoomType resultDormitoryRoomType = resultDormitoryTerm.getDormitoryRoomType();
                Dormitory resultDormitory = resultDormitoryRoomType.getDormitory();
                RoomType resultRoomType = resultDormitoryRoomType.getRoomType();
                resultDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                        .dormitoryName(resultDormitory.getName())
                        .roomSize(resultRoomType.getRoomSize())
                        .build();
            }

            DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(user.getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
                    .address(student.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
                    .dormitoryApplicationResult(DormitoryApplicationResult.WAIT)
                    .build();

            dormitoryApplicationWebResList.add(dormitoryApplicationWebRes);
        }

        return dormitoryApplicationWebResList;
    }

    public List<DormitoryApplicationWebRes> findDormitoryApplicationsById(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        if (dormitoryApplicationSetting.getApplicationStatus().equals(ApplicationStatus.READY) || dormitoryApplicationSetting.getApplicationStatus().equals(ApplicationStatus.NOW))
            throw new IllegalArgumentException("종료된 입사 신청 설정만 조회할 수 있습니다.");

        if(!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitoryApplicationWebRes> dormitoryApplicationWebResList = new ArrayList<>();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            User user = student.getUser();

            DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
            DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
            Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
            RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                    .dormitoryName(applicationDormitory.getName())
                    .roomSize(applicationRoomType.getRoomSize())
                    .build();

            DormitoryTerm resultDormitoryTerm = dormitoryApplication.getResultDormitoryTerm();
            DormitoryRoomType resultDormitoryRoomType = resultDormitoryTerm.getDormitoryRoomType();
            Dormitory resultDormitory = resultDormitoryRoomType.getDormitory();
            RoomType resultRoomType = resultDormitoryRoomType.getRoomType();
            DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes;
            if (dormitoryApplication.getDormitoryApplicationResult().equals(DormitoryApplicationResult.NON_PASS) || dormitoryApplication.getDormitoryApplicationResult().equals(DormitoryApplicationResult.WAIT))
                resultDormitoryRoomTypeRes = null;
            else {
                resultDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                        .dormitoryName(resultDormitory.getName())
                        .roomSize(resultRoomType.getRoomSize())
                        .build();
            }

            DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
                    .dormitoryApplicationId(dormitoryApplication.getId())
                    .studentName(user.getName())
                    .studentNumber(student.getStudentNumber())
                    .gender(student.getGender())
                    .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
                    .address(student.getAddress())
                    .copy(dormitoryApplication.getCopy())
                    .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
                    .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
                    .dormitoryApplicationResult(DormitoryApplicationResult.WAIT)
                    .build();
            dormitoryApplicationWebResList.add(dormitoryApplicationWebRes);
        }

        return dormitoryApplicationWebResList;
    }

    public List<InspectDormitoryApplicationRes> inspectApplication(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId, ApplicationIdsReq applicationIdsReq) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        List<InspectDormitoryApplicationRes> inspectDormitoryApplicationResList = new ArrayList<>(); // 응답 DTO List

        /*
            TODO
             0. 데이터 세팅
               - 0-0)요청으로 받은 입사 신청 id 전체 조회
               - 0-1)기준 설정
               - 0-2)기숙사 + 인실/성별 (Dormitory Room Type) 별 선발 인원 (1)  /  Dormitory Room Type 별로 분류할 List 생성 (수용 인원)  /  Dormitory Room Type 별 입사 신청 (Dormitory Application) List 생성
             1. 전원 합격 대상 합격 처리
               - 우선 선발 대상자 : '입사 신청'에 우선 선발 증빙 서류 여부 확인  ->  (1) 인원 수 변경
             2. 신입생 / 재학생 분기
               - 신입생 학번 -> 10자리 / 재학생 학번 -> 8자리
             2-1) 신입생
              - 신입생 전원 합격의 경우 -> (1) 인원 수 변경
              - 장거리 우선 합격 : 거리 기준 100% 가정 후 "[최종 점수 산정]"  ==>  Dormitory Room Type에 따라 각 List에 add ==> 재학생과 동일
                - 동점자 Random( 신청 순서 )으로 랭크 결정 ( RANK )
             2-2) 재학생
              - 상/벌점 점수 활성화 여부 확인 후 "[최종 점수 산정]"  ==>  Dormitory Room Type에 따라 각 List에 add
                - 기준에 따라 동점자 처리 ( RANK )
             3. 합/불/이동합격 선발
               - 각 List 별 수용 인원 고려하여 커트라인 산정 후 합격자 선발 (1) 인원 수 변경
                 - 동점자 고려 (커트라인 점수만)
               - 이동 합격 활성화 시 다른 List 배정
             4. 응답 형식 고민
               - 다 줄 필요는 x인듯 -> 이름 등은 냅두고.. :: 리스트(입사 신청 id, 결과 건물, 결과만?) + (기숙사 + 방 타입) Dormitory Room Type (ID) 별 남은 인원
               - 조회처럼 완전하게
               - Dormitory Application ID, 배정 건물, 배정 결과만
               + 공통) Dormitory Room Type 별 남은 인원
         */
        // 0. 데이터 세팅
        // 0-0) 요청으로 받은 입사 신청 id 전체 조회
        List<DormitoryApplication> dormitoryApplicationList = dormitoryApplicationRepository.findAllById(applicationIdsReq.getDormitoryApplicationIds());
        List<DormitoryApplication> removeDormitoryApplicationList = new ArrayList<>();
        // 0-1) 기준 설정
        StandardSetting standardSetting = standardSettingRepository.findBySchool(school)
                .orElseThrow(StandardSettingNotFoundException::new);
        // 거리 점수 목록 가져오기
        List<Region> regionList = regionRepository.findAll();
        Map<Long, Double> regionDistanceScoreMap = new HashMap<>();
        for (Region region : regionList) {
            // 각 Region에 해당하는 RegionDistanceScore 목록을 가져옵니다.
            Optional<RegionDistanceScore> findRegionDistanceScore = regionDistanceScoreRepository.findBySchoolAndRegionWithDistanceScore(school, region);
            Double score = 4.5;
            if (findRegionDistanceScore.isPresent())
                score = findRegionDistanceScore.get().getDistanceScore().getScore();
            // 각 Region에 대해 거리 점수를 가져와 Map에 추가
            regionDistanceScoreMap.put(region.getId(), score);
        }

        // 0-2) 기숙사 + 인실/성별 (Dormitory Room Type) 별 선발 인원  /  Dormitory Room Type 별로 분류할 List 생성
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId)
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        List<DormitorySettingTerm> dormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        Map<Long, Integer> acceptLimitMap = new HashMap<>(); // KEY : Dormitory Room Type's ID / VALUE : Accept Limit
        // 1st KEY : Dormitory Room Type's ID, 2nd KEY : Dormitory Application's ID / VALUE : 환산 점수
        Map<Long, List<Map<Long, Double>>> applicationListMapByDormitoryRoomType = new HashMap<>();
        for (DormitorySettingTerm dormitorySettingTerm : dormitorySettingTermList) {
            DormitoryRoomType dormitoryRoomType = dormitorySettingTerm.getDormitoryRoomType();
            acceptLimitMap.put(dormitoryRoomType.getId(), dormitorySettingTerm.getAcceptLimit());
            applicationListMapByDormitoryRoomType.put(dormitoryRoomType.getId(), new ArrayList<>());
        }


        // 1. 전원 합격 대상 합격 처리 - 우선 선발 대상자 : '입사 신청'에 우선 선발 증빙 서류 여부 확인 : 선발 인원 수 변경
        if (standardSetting.isPrioritySelection()) {
            for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
                if (dormitoryApplication.getPrioritySelectionCopy() == null) continue;
//                pass(dormitoryApplication, acceptLimitMap, dormitoryApplicationList, dormitoryApplicationWebResList);
                pass(dormitoryApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);
            }
        }
        dormitoryApplicationList.removeAll(removeDormitoryApplicationList);
        removeDormitoryApplicationList.clear();

        // 2. 신입생 / 재학생 분기 : 신입생 학번 -> 10자리 / 재학생 학번 -> 8자리
        FreshmanStandard freshmanStandard = standardSetting.getFreshmanStandard();
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            Student student = dormitoryApplication.getStudent();
            if (student.getStudentNumber().length() != 8) {
                // 신입생
                if (FreshmanStandard.EVERYONE.equals(freshmanStandard))  // 신입생 전원 합격
                    pass(dormitoryApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);
            } else {
                // 재학생 + 신입생 장거리 우선 합격 : 상/벌점 점수 활성화 여부 확인 후 "[최종 점수 산정]"  ==>  Dormitory Room Type에 따라 각 List에 add
                int bonusPoint = 0;
                int minusPoint = 0;
                if (standardSetting.isPointReflection()) {
                    bonusPoint += student.getBonusPoint();
                    minusPoint += student.getMinusPoint();
                }
                // 상/벌점은 체크 안 한 경우 효율을 위해 밖으로 뺄 것 / 어쩄든 iter if 돌긴 해야 함 ..
                // ----------------------------------------------------------------------------
                // 1st : 일치 지역 점수 가져오기  /  2nd : 일치 없다면 -> 부모 지역 점수 가져오기  /  3rd : 그래도 없다면 -> 만점 처리
                Region studentRegion = student.getRegion();
                Double distanceScore = regionDistanceScoreMap.get(studentRegion.getId());
                if (distanceScore == null) {
                    Region studentParentRegion = studentRegion.getParentRegion();
                    distanceScore = regionDistanceScoreMap.get(studentParentRegion.getId());
                    if (distanceScore == null)
                        distanceScore = 4.5;
                }
                Double totalScore = bonusPoint - minusPoint + distanceScore;

                DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
                DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
                List<Map<Long, Double>> dormitoryApplicationMapList = applicationListMapByDormitoryRoomType.get(applicationDormitoryRoomType.getId());
                Map<Long, Double> dormitoryApplicationMap = new HashMap<>();
                dormitoryApplicationMap.put(dormitoryApplication.getId(), totalScore);
                dormitoryApplicationMapList.add(dormitoryApplicationMap);
            }
        }
        dormitoryApplicationList.removeAll(removeDormitoryApplicationList);
        removeDormitoryApplicationList.clear();

        // 3. 합/불/이동합격 선발
        for (Map.Entry<Long, List<Map<Long, Double>>> entry : applicationListMapByDormitoryRoomType.entrySet()) {
            // Entry에서 List<Map<Long, Double>>를 가져옴
            List<Map<Long, Double>> applicationList = entry.getValue();
            // Map<Long, Double>를 List로 변환하여 정렬
            List<Map.Entry<Long, Double>> sortedApplications = applicationList.stream() // 입사 신청 - 환산 점수
                    .flatMap(map -> map.entrySet().stream())  // Map<Long, Double> -> Map.Entry<Long, Double>로 변환
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  // Double 값으로 내림차순 정렬
                    .collect(Collectors.toList());

            Integer acceptLimit = acceptLimitMap.get(entry.getKey());
            boolean needCut = acceptLimit < sortedApplications.size();
            if (!needCut) { // 수용 인원 >= 신청자  : 자리 남는 경우
                for (Map.Entry<Long, Double> sortedApplication : sortedApplications)
                    sortedPass(sortedApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);

            } else { // 수용 인원 < 신청자
                for (int i = 0; i < acceptLimit; i++) {
                    Map.Entry<Long, Double> sortedApplication = sortedApplications.get(i);
                    sortedPass(sortedApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);
                }
            }
        }
        dormitoryApplicationList.removeAll(removeDormitoryApplicationList);
        removeDormitoryApplicationList.clear();

        Map<Long, Double> movePassMap = new HashMap<>(); // 입사 신청 ID, 환산 점수 Map : 합격자 모두 거르고 Dormitory Room Type에 관계 없이 정렬하기 위함 (이동 합격)
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) { // 불합격 입사 신청 (이동 합격 전)
            Long dormitoryApplicationId = dormitoryApplication.getId();
            DormitoryRoomType applicationDormitoryRoomType = dormitoryApplication.getApplicationDormitoryTerm().getDormitoryRoomType();
            List<Map<Long, Double>> applicationScoreMapList = applicationListMapByDormitoryRoomType.get(applicationDormitoryRoomType.getId());
            for (Map<Long, Double> applicationScoreMap : applicationScoreMapList) {
                if (applicationScoreMap.containsKey(dormitoryApplicationId)) {
                    Double totalScore = applicationScoreMap.get(dormitoryApplicationId);
                    movePassMap.put(dormitoryApplicationId, totalScore);
                    break;
                }
            }
        }

        // Map의 EntrySet을 이용하여 List로 변환 후 값으로 내림차순 정렬
        List<Map.Entry<Long, Double>> sortedEntries = movePassMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  // Double 값 기준 내림차순 정렬
                .collect(Collectors.toList());

        // 수용 인원이 남아있는 Dormitory Room Type에 랜덤 배정
        if (movePassMap.size() != 0) { // movePassMap == 0 인 경우 들어가면 안 됨 / accept Limit만큼 다 돌아도 안 됨 (인원만큼 돌아야 함)
            for (Map.Entry<Long, Integer> acceptLimitEntry : acceptLimitMap.entrySet()) {
                Integer acceptLimit = acceptLimitEntry.getValue();
                int num = Math.min(acceptLimit, movePassMap.size());
                if (num <= 0) continue;
                // 자리 있는 Dormitory Room Type
                DormitoryRoomType resultDormitoryRoomType = dormitoryRoomTypeRepository.findById(acceptLimitEntry.getKey())
                        .orElseThrow();
                for (int i = num; i > 0; i--) {
                    DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findById(sortedEntries.get(i).getKey())
                            .orElseThrow();
                    movePass(resultDormitoryRoomType, dormitoryApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);
                }
            }
            dormitoryApplicationList.removeAll(removeDormitoryApplicationList);
            removeDormitoryApplicationList.clear();
        }

        // 불합격자 DTO
        for (DormitoryApplication dormitoryApplication : dormitoryApplicationList) {
            InspectDormitoryApplicationRes inspectDormitoryApplicationRes = getNonPassDormitoryApplicationWebRes(dormitoryApplication);
            inspectDormitoryApplicationResList.add(inspectDormitoryApplicationRes);
        }

        return inspectDormitoryApplicationResList;
    }

    private InspectDormitoryApplicationRes getNonPassDormitoryApplicationWebRes(DormitoryApplication dormitoryApplication) {
//        Student student = dormitoryApplication.getStudent();
//        User user = student.getUser();
//        DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
//        DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();

//        Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
//        RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
//        DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
//                .dormitoryName(applicationDormitory.getName())
//                .roomSize(applicationRoomType.getRoomSize())
//                .build();

        return InspectDormitoryApplicationRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .resultDormitoryRoomTypeRes(null)
                .dormitoryApplicationResult(DormitoryApplicationResult.NON_PASS)
                .build();

//        DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
//                .dormitoryApplicationId(dormitoryApplication.getId())
//                .studentName(user.getName())
//                .studentNumber(student.getStudentNumber())
//                .gender(student.getGender())
//                .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
//                .address(student.getAddress())
//                .copy(dormitoryApplication.getCopy())
//                .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
//                .resultDormitoryRoomTypeRes(null)
//                .dormitoryApplicationResult(DormitoryApplicationResult.NON_PASS)
//                .build();
//        return dormitoryApplicationWebRes;
    }


    // 이동 합격 배정
    private void movePass(DormitoryRoomType resultDormitoryRoomType, DormitoryApplication dormitoryApplication, Map<Long, Integer> acceptLimitMap, List<DormitoryApplication> removeDormitoryApplicationList, List<InspectDormitoryApplicationRes> inspectDormitoryApplicationResList) {
        Integer acceptLimit = acceptLimitMap.get(resultDormitoryRoomType.getId());
        acceptLimitMap.replace(resultDormitoryRoomType.getId(), acceptLimit - 1);

        InspectDormitoryApplicationRes inspectDormitoryApplicationRes = getMovePassDormitoryApplicationWebRes(dormitoryApplication, resultDormitoryRoomType);

        removeDormitoryApplicationList.add(dormitoryApplication);
        inspectDormitoryApplicationResList.add(inspectDormitoryApplicationRes);
    }

    private static InspectDormitoryApplicationRes getMovePassDormitoryApplicationWebRes(DormitoryApplication dormitoryApplication, DormitoryRoomType resultDormitoryRoomType) {
        Student student = dormitoryApplication.getStudent();
        User user = student.getUser();
        DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
        DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();

//        Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
//        RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
//        DormitoryApplicationWebRes.DormitoryRoomTypeRes applicationDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
//                .dormitoryName(applicationDormitory.getName())
//                .roomSize(applicationRoomType.getRoomSize())
//                .build();

        Dormitory resultDormitory = resultDormitoryRoomType.getDormitory();
        RoomType resultRoomType = resultDormitoryRoomType.getRoomType();
        DormitoryApplicationWebRes.DormitoryRoomTypeRes resultDormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                .dormitoryName(resultDormitory.getName())
                .roomSize(resultRoomType.getRoomSize())
                .build();

        return InspectDormitoryApplicationRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
                .dormitoryApplicationResult(DormitoryApplicationResult.MOVE_PASS)
                .build();

//        DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
//                .dormitoryApplicationId(dormitoryApplication.getId())
//                .studentName(user.getName())
//                .studentNumber(student.getStudentNumber())
//                .gender(student.getGender())
//                .applicationDormitoryRoomTypeRes(applicationDormitoryRoomTypeRes)
//                .address(student.getAddress())
//                .copy(dormitoryApplication.getCopy())
//                .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
//                .resultDormitoryRoomTypeRes(resultDormitoryRoomTypeRes)
//                .dormitoryApplicationResult(DormitoryApplicationResult.MOVE_PASS)
//                .build();
//        return dormitoryApplicationWebRes;
    }

    // 정렬된 입사 신청 합격 처리
    private void sortedPass(Map.Entry<Long, Double> sortedApplication, Map<Long, Integer> acceptLimitMap, List<DormitoryApplication> removeDormitoryApplicationList, List<InspectDormitoryApplicationRes> inspectDormitoryApplicationResList) {
        Long dormitoryApplicationId = sortedApplication.getKey();
        DormitoryApplication dormitoryApplication = dormitoryApplicationRepository.findById(dormitoryApplicationId)
                .orElseThrow();
        pass(dormitoryApplication, acceptLimitMap, removeDormitoryApplicationList, inspectDormitoryApplicationResList);
    }

    // (전원) 합격의 경우 사용. (우선 선발 /신입생 전원 합격 포함)
    private static void pass(DormitoryApplication dormitoryApplication, Map<Long, Integer> acceptLimitMap, List<DormitoryApplication> removeDormitoryApplicationList, List<InspectDormitoryApplicationRes> inspectDormitoryApplicationResList) {
        DormitoryTerm applicationDormitoryTerm = dormitoryApplication.getApplicationDormitoryTerm();
        DormitoryRoomType applicationDormitoryRoomType = applicationDormitoryTerm.getDormitoryRoomType();
        Integer acceptLimit = acceptLimitMap.get(applicationDormitoryRoomType.getId());
        acceptLimitMap.replace(applicationDormitoryRoomType.getId(), acceptLimit - 1);

        InspectDormitoryApplicationRes inspectDormitoryApplicationRes = getPassDormitoryApplicationWebRes(dormitoryApplication, applicationDormitoryRoomType);

        removeDormitoryApplicationList.add(dormitoryApplication);
        inspectDormitoryApplicationResList.add(inspectDormitoryApplicationRes);
    }

    private static InspectDormitoryApplicationRes getPassDormitoryApplicationWebRes(DormitoryApplication dormitoryApplication, DormitoryRoomType applicationDormitoryRoomType) {
        Student student = dormitoryApplication.getStudent();
        User user = student.getUser();
        Dormitory applicationDormitory = applicationDormitoryRoomType.getDormitory();
        RoomType applicationRoomType = applicationDormitoryRoomType.getRoomType();
        DormitoryApplicationWebRes.DormitoryRoomTypeRes dormitoryRoomTypeRes = DormitoryApplicationWebRes.DormitoryRoomTypeRes.builder()
                .dormitoryName(applicationDormitory.getName())
                .roomSize(applicationRoomType.getRoomSize())
                .build();

        return InspectDormitoryApplicationRes.builder()
                .dormitoryApplicationId(dormitoryApplication.getId())
                .resultDormitoryRoomTypeRes(dormitoryRoomTypeRes)
                .dormitoryApplicationResult(DormitoryApplicationResult.PASS)
                .build();

//        DormitoryApplicationWebRes dormitoryApplicationWebRes = DormitoryApplicationWebRes.builder()
//                .dormitoryApplicationId(dormitoryApplication.getId())
//                .studentName(user.getName())
//                .studentNumber(student.getStudentNumber())
//                .gender(student.getGender())
//                .applicationDormitoryRoomTypeRes(dormitoryRoomTypeRes)
//                .address(student.getAddress())
//                .copy(dormitoryApplication.getCopy())
//                .prioritySelectionCopy(dormitoryApplication.getPrioritySelectionCopy())
//                .resultDormitoryRoomTypeRes(dormitoryRoomTypeRes)
//                .dormitoryApplicationResult(DormitoryApplicationResult.PASS)
//                .build();
//        return dormitoryApplicationWebRes;
    }

    // Description : 합/불 저장 ( BULK UPDATE )
    @Transactional
    public void modifyApplicationResult(ModifyApplicationResultIdsReq ModifyApplicationResultIdsReq) {
        List<ApplicationResultIdsReq> applicationResultIdsReqList = ModifyApplicationResultIdsReq.getApplicationResultIdsReqList();
        for (ApplicationResultIdsReq applicationResultIdsReq : applicationResultIdsReqList) {
            DormitoryApplicationResult dormitoryApplicationResult = applicationResultIdsReq.getDormitoryApplicationResult();
            List<Long> dormitoryApplicationIds = applicationResultIdsReq.getDormitoryApplicationIds();
            dormitoryApplicationRepository.updateDormitoryApplicationResults(dormitoryApplicationResult, dormitoryApplicationIds);
        }
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(ModifyApplicationResultIdsReq.getDormitoryApplicationSettingId())
                .orElseThrow(DormitoryApplicationSettingNotFoundException::new);
        dormitoryApplicationSetting.updateApplicationStatus(ApplicationStatus.DEPOSIT);
    }
}
