package dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.service;

import dormease.dormeasedev.domain.dormitories.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitories.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.dto.request.DormitoryRoomTypeReq;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.DormitoryRoomType;
import dormease.dormeasedev.domain.dormitories.dormitory_room_type.domain.repository.DormitoryRoomTypeRepository;
import dormease.dormeasedev.domain.dormitories.room_type.domain.RoomType;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.ApplicationStatus;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.DormitoryApplicationSetting;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.domain.repository.DormitoryApplicationSettingRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request.CreateDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.request.ModifyDormitoryApplicationSettingReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryApplicationSettingSimpleRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.DormitoryRoomTypeRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingHistoryRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.dto.response.FindDormitoryApplicationSettingRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_application_setting.exception.DormitoryApplicationSettingNotFoundException;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.DormitorySettingTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.domain.repository.DormitorySettingTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto.DormitorySettingTermRes;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_setting_term.dto.request.ModifyDormitorySettingTermReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.DormitoryTerm;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.domain.repository.DormitoryTermRepository;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.request.DormitoryTermReq;
import dormease.dormeasedev.domain.dormitory_applications.dormitory_term.dto.response.TermRes;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.MealTicket;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.domain.repository.MealTicketRepository;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.request.MealTicketReq;
import dormease.dormeasedev.domain.dormitory_applications.meal_ticket.dto.response.MealTicketRes;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.Term;
import dormease.dormeasedev.domain.dormitory_applications.term.domain.repository.TermRepository;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.request.ModifyTermReq;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.request.TermReq;
import dormease.dormeasedev.domain.dormitory_applications.term.dto.response.DormitoryTermRes;
import dormease.dormeasedev.domain.school.domain.School;
import dormease.dormeasedev.domain.users.user.domain.User;
import dormease.dormeasedev.domain.users.user.exception.InvalidSchoolAuthorityException;
import dormease.dormeasedev.domain.users.user.service.UserService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.exception.DefaultAssert;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DormitoryApplicationSettingService {

    private final DormitoryApplicationSettingRepository dormitoryApplicationSettingRepository;
    private final TermRepository termRepository;
    private final DormitorySettingTermRepository dormitorySettingTermRepository;
    private final MealTicketRepository mealTicketRepository;
    private final DormitoryTermRepository dormitoryTermRepository;
    private final DormitoryRoomTypeRepository dormitoryRoomTypeRepository;
    private final DormitoryRepository dormitoryRepository;

    private final UserService userService;

    // Description : 현재 입사 신청 설정 조회
    public ResponseEntity<?> findNowDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();
        DormitoryApplicationSetting dormitoryApplicationSetting = dormitoryApplicationSettingRepository.findAllBySchoolAndApplicationStatusIn(
                school,
                Arrays.asList(ApplicationStatus.READY, ApplicationStatus.NOW)
                ).orElseThrow(DormitoryApplicationSettingNotFoundException::new);

        // 식권 조회
        List<MealTicketRes> mealTicketResList = new ArrayList<>();
        List<MealTicket> mealTicketList = mealTicketRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        for (MealTicket mealTicket : mealTicketList) {
            MealTicketRes mealTicketRes = MealTicketRes.builder()
                    .id(mealTicket.getId())
                    .count(mealTicket.getCount())
                    .price(mealTicket.getPrice())
                    .build();
            mealTicketResList.add(mealTicketRes);
        }

        // 기숙사_방타입 관련
        List<DormitorySettingTerm> findDormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitorySettingTermRes> dormitorySettingTermResList = new ArrayList<>();
        for (DormitorySettingTerm dormitorySettingTerm : findDormitorySettingTermList) {
            DormitoryRoomType dormitoryRoomType = dormitorySettingTerm.getDormitoryRoomType();
            Dormitory dormitory = dormitoryRoomType.getDormitory();
            RoomType roomType = dormitoryRoomType.getRoomType();

            DormitorySettingTermRes dormitorySettingTermRes = DormitorySettingTermRes.builder()
                    .dormitorySettingTermId(dormitorySettingTerm.getId())
                    .dormitoryRoomTypeId(dormitoryRoomType.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(roomType.getRoomSize())
                    .gender(roomType.getGender())
                    .acceptLimit(dormitorySettingTerm.getAcceptLimit())
                    .build();
            dormitorySettingTermResList.add(dormitorySettingTermRes);
        }

        // 거주기간 관련
        List<Term> termList = termRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<TermRes> termResList = new ArrayList<>();
        for (Term term : termList) {
            List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
            List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();
            for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
                DormitoryTermRes dormitoryTermRes = DormitoryTermRes.builder()
                        .dormitoryTermId(dormitoryTerm.getId())
                        .dormitoryRoomTypeId(dormitoryTerm.getDormitoryRoomType().getId())
                        .price(dormitoryTerm.getPrice())
                        .build();
                dormitoryTermResList.add(dormitoryTermRes);
            }

            TermRes termRes = TermRes.builder()
                    .termId(term.getId())
                    .termName(term.getTermName())
                    .startDate(term.getStartDate())
                    .endDate(term.getEndDate())
                    .dormitoryTermResList(dormitoryTermResList)
                    .build();
            termResList.add(termRes);
        }

        // 입사 신청 설정 조회
        FindDormitoryApplicationSettingRes findDormitoryApplicationSettingRes = FindDormitoryApplicationSettingRes.builder()
                .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                .title(dormitoryApplicationSetting.getTitle())
                .startDate(dormitoryApplicationSetting.getStartDate())
                .endDate(dormitoryApplicationSetting.getEndDate())
                .depositStartDate(dormitoryApplicationSetting.getDepositStartDate())
                .depositEndDate(dormitoryApplicationSetting.getDepositEndDate())
                .securityDeposit(dormitoryApplicationSetting.getSecurityDeposit())
                .applicationStatus(dormitoryApplicationSetting.getApplicationStatus())
                .dormitorySettingTermResList(dormitorySettingTermResList)
                .termResList(termResList)
                .mealTicketResList(mealTicketResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 생성
    @Transactional
    public ResponseEntity<?> createDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, CreateDormitoryApplicationSettingReq createDormitoryApplicationSettingReq) {

        User user = userService.validateUserById(userDetailsImpl.getUserId());
        School school = user.getSchool();

        // 입사 신청 설정 save
        DormitoryApplicationSetting dormitoryApplicationSetting = DormitoryApplicationSetting.builder()
                .school(school)
                .title(createDormitoryApplicationSettingReq.getTitle())
                .startDate(createDormitoryApplicationSettingReq.getStartDate())
                .endDate(createDormitoryApplicationSettingReq.getEndDate())
                .depositStartDate(createDormitoryApplicationSettingReq.getDepositStartDate())
                .depositEndDate(createDormitoryApplicationSettingReq.getDepositEndDate())
                .securityDeposit(createDormitoryApplicationSettingReq.getSecurityDeposit())
                .build();
        dormitoryApplicationSettingRepository.save(dormitoryApplicationSetting);

        // 식권 save : MealTicketReq - 삭권 관련 설정
        for (MealTicketReq mealTicketReq : createDormitoryApplicationSettingReq.getMealTicketReqList()) {
            MealTicket mealTicket = MealTicket.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .count(mealTicketReq.getCount())
                    .price(mealTicketReq.getPrice())
                    .build();
            mealTicketRepository.save(mealTicket);
        }

        // DormitoryRoomTypeReq - 기숙사 자체 설정 관련
        for (DormitoryRoomTypeReq dormitoryRoomTypeReq : createDormitoryApplicationSettingReq.getDormitoryRoomTypeReqList()) {
            if (dormitoryRoomTypeReq.getAcceptLimit().equals(0))
                continue;
            DormitoryRoomType dormitoryRoomType = dormitoryRoomTypeRepository.findById(dormitoryRoomTypeReq.getDormitoryRoomTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 DormitoryRoomTypeId 입니다."));

            // DormitorySettingTerm save - 기숙사_방타입 : 입사 신청 설정 M:N 관계 중간 테이블
            DormitorySettingTerm dormitorySettingTerm = DormitorySettingTerm.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .dormitoryRoomType(dormitoryRoomType)
                    .acceptLimit(dormitoryRoomTypeReq.getAcceptLimit())
                    .build();
            dormitorySettingTermRepository.save(dormitorySettingTerm);
        }

        for (TermReq termReq : createDormitoryApplicationSettingReq.getTermReqList()) {
            Term term = Term.builder()
                    .dormitoryApplicationSetting(dormitoryApplicationSetting)
                    .termName(termReq.getTermName())
                    .startDate(termReq.getStartDate())
                    .endDate(termReq.getEndDate())
                    .build();
            termRepository.save(term);

            for (DormitoryTermReq dormitoryTermReq : termReq.getDormitoryTermReqList()) {
                // 아래 if문 예외 처리는 ui 변경하여 입사신청 안받을 경우 0명 기입 대신 아예 칸을 없애는 방향으로 논의!
                if (dormitoryTermReq.getPrice() == null)
                    continue;

                Optional<DormitoryRoomType> findDormitoryRoomType = dormitoryRoomTypeRepository.findById(dormitoryTermReq.getDormitoryRoomTypeId());
                DefaultAssert.isTrue(findDormitoryRoomType.isPresent(), "올바르지 않은 DormitoryRoomTypeId 입니다.");
                DormitoryRoomType dormitoryRoomType = findDormitoryRoomType.get();

                DormitoryTerm dormitoryTerm = DormitoryTerm.builder()
                        .term(term)
                        .dormitoryRoomType(dormitoryRoomType)
                        .price(dormitoryTermReq.getPrice())
                        .build();
                dormitoryTermRepository.save(dormitoryTerm);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("입사 신청 설정이 완료되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 조회
    public ResponseEntity<?> findDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, Long dormitoryApplicationSettingId) {

        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        DefaultAssert.isTrue(dormitoryApplicationSetting.getSchool().equals(school), "다른 학교의 입사 신청 설정을 조회할 수 없습니다.");

        // 식권 조회
        List<MealTicketRes> mealTicketResList = new ArrayList<>();
        List<MealTicket> mealTicketList = mealTicketRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        for (MealTicket mealTicket : mealTicketList) {
            MealTicketRes mealTicketRes = MealTicketRes.builder()
                    .id(mealTicket.getId())
                    .count(mealTicket.getCount())
                    .price(mealTicket.getPrice())
                    .build();
            mealTicketResList.add(mealTicketRes);
        }

        // 기숙사_방타입 관련
        List<DormitorySettingTerm> findDormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<DormitorySettingTermRes> dormitorySettingTermResList = new ArrayList<>();
        for (DormitorySettingTerm dormitorySettingTerm : findDormitorySettingTermList) {
            DormitoryRoomType dormitoryRoomType = dormitorySettingTerm.getDormitoryRoomType();
            Dormitory dormitory = dormitoryRoomType.getDormitory();
            RoomType roomType = dormitoryRoomType.getRoomType();

            DormitorySettingTermRes dormitorySettingTermRes = DormitorySettingTermRes.builder()
                    .dormitorySettingTermId(dormitorySettingTerm.getId())
                    .dormitoryRoomTypeId(dormitoryRoomType.getId())
                    .dormitoryName(dormitory.getName())
                    .roomSize(roomType.getRoomSize())
                    .gender(roomType.getGender())
                    .acceptLimit(dormitorySettingTerm.getAcceptLimit())
                    .build();
            dormitorySettingTermResList.add(dormitorySettingTermRes);
        }

        // 거주기간 관련
        List<Term> termList = termRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<TermRes> termResList = new ArrayList<>();
        for (Term term : termList) {
            List<DormitoryTerm> dormitoryTermList = dormitoryTermRepository.findByTerm(term);
            List<DormitoryTermRes> dormitoryTermResList = new ArrayList<>();
            for (DormitoryTerm dormitoryTerm : dormitoryTermList) {
                DormitoryTermRes dormitoryTermRes = DormitoryTermRes.builder()
                        .dormitoryTermId(dormitoryTerm.getId())
                        .dormitoryRoomTypeId(dormitoryTerm.getDormitoryRoomType().getId())
                        .price(dormitoryTerm.getPrice())
                        .build();
                dormitoryTermResList.add(dormitoryTermRes);
            }

            TermRes termRes = TermRes.builder()
                    .termId(term.getId())
                    .termName(term.getTermName())
                    .startDate(term.getStartDate())
                    .endDate(term.getEndDate())
                    .dormitoryTermResList(dormitoryTermResList)
                    .build();
            termResList.add(termRes);
        }

        // 입사 신청 설정 조회
        FindDormitoryApplicationSettingRes findDormitoryApplicationSettingRes = FindDormitoryApplicationSettingRes.builder()
                .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                .title(dormitoryApplicationSetting.getTitle())
                .startDate(dormitoryApplicationSetting.getStartDate())
                .endDate(dormitoryApplicationSetting.getEndDate())
                .depositStartDate(dormitoryApplicationSetting.getDepositStartDate())
                .depositEndDate(dormitoryApplicationSetting.getDepositEndDate())
                .securityDeposit(dormitoryApplicationSetting.getSecurityDeposit())
                .applicationStatus(dormitoryApplicationSetting.getApplicationStatus())
                .dormitorySettingTermResList(dormitorySettingTermResList)
                .termResList(termResList)
                .mealTicketResList(mealTicketResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 수정
    @Transactional
    public void modifyDormitoryApplicationSetting(UserDetailsImpl userDetailsImpl, ModifyDormitoryApplicationSettingReq modifyDormitoryApplicationSettingReq, Long dormitoryApplicationSettingId) {
        DormitoryApplicationSetting dormitoryApplicationSetting = validateDormitoryApplicationSettingById(dormitoryApplicationSettingId);
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        if(!dormitoryApplicationSetting.getSchool().equals(school))
            throw new InvalidSchoolAuthorityException();

        // 입사 신청 설정 기본 정보 수정
        dormitoryApplicationSetting.updateDormitoryApplicationSetting(
                modifyDormitoryApplicationSettingReq.getTitle(),
                modifyDormitoryApplicationSettingReq.getStartDate(),
                modifyDormitoryApplicationSettingReq.getEndDate(),
                modifyDormitoryApplicationSettingReq.getDepositStartDate(),
                modifyDormitoryApplicationSettingReq.getDepositEndDate(),
                modifyDormitoryApplicationSettingReq.getSecurityDeposit()
        );

        // 기존 MealTicket 목록 조회
        List<MealTicket> findMealTicketList = mealTicketRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        // 요청받은 MealTicket 목록과 비교하여 업데이트 및 생성
        List<MealTicket> updatedMealTickets = modifyDormitoryApplicationSettingReq.getModifyMealTicketReqList().stream()
                .map(mealTicketReq -> {
                    // 기존에 존재하는 MealTicket 찾기
                    return findMealTicketList.stream()
                            .filter(findMealTicket -> findMealTicket.getId().equals(mealTicketReq.getMealTicketId()))
                            .findFirst()
                            .map(findMealTicket -> {
                                // 존재하면 업데이트
                                findMealTicket.updateMealTicket(mealTicketReq.getCount(), mealTicketReq.getPrice());
                                return findMealTicket;
                            })
                            .orElseGet(() -> {
                                // 존재하지 않으면 새로운 MealTicket 생성
                                return MealTicket.builder()
                                        .dormitoryApplicationSetting(dormitoryApplicationSetting)
                                        .count(mealTicketReq.getCount())
                                        .price(mealTicketReq.getPrice())
                                        .build();
                            });
                })
                .collect(Collectors.toList());

        // 삭제할 MealTicket 목록 추출
        List<MealTicket> removeMealTicketList = findMealTicketList.stream()
                .filter(findMealTicket -> updatedMealTickets.stream()
                        .noneMatch(updatedMealTicket -> updatedMealTicket.getId().equals(findMealTicket.getId())))
                .collect(Collectors.toList());

        // 삭제할 MealTicket 삭제
        mealTicketRepository.deleteAll(removeMealTicketList);
        // 변경된 데이터 저장 (추가 및 수정)
        mealTicketRepository.saveAll(updatedMealTickets);

        List<DormitorySettingTerm> findDormitorySettingTermList = dormitorySettingTermRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);

        // 최적화
        // 1. 요청에서 필요한 dormitoryRoomTypeId 목록 추출
        List<Long> roomTypeIds = modifyDormitoryApplicationSettingReq.getModifyDormitorySettingTermReqList().stream()
                .map(ModifyDormitorySettingTermReq::getDormitoryRoomTypeId)
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        // 2. 한 번의 DB 호출로 필요한 DormitoryRoomType 목록 조회
        List<DormitoryRoomType> roomTypes = dormitoryRoomTypeRepository.findAllById(roomTypeIds);

        // 3. 조회한 결과를 맵에 캐싱
        Map<Long, DormitoryRoomType> roomTypeMap = roomTypes.stream()
                .collect(Collectors.toMap(DormitoryRoomType::getId, Function.identity()));

        List<DormitorySettingTerm> updatedDormitorySettingTermList = modifyDormitoryApplicationSettingReq.getModifyDormitorySettingTermReqList().stream()
                .map(dormitorySettingTermReq -> {
                    return findDormitorySettingTermList.stream()
                            .filter(findDormitorySettingTerm -> findDormitorySettingTerm.getId().equals(dormitorySettingTermReq.getDormitorySettingTermId()))
                            .findFirst()
                            .map(findDormitorySettingTerm -> {
                                // 존재하면 업데이트
                                findDormitorySettingTerm.updateAcceptLimit(dormitorySettingTermReq.getAcceptLimit());
                                return findDormitorySettingTerm;
                            })
                            .orElseGet(() -> {
                                // 존재하지 않으면 새로운 DormitorySettingTerm 생성
                                DormitoryRoomType dormitoryRoomType = roomTypeMap.get(dormitorySettingTermReq.getDormitoryRoomTypeId());
                                if (dormitoryRoomType == null) {
                                    throw new IllegalArgumentException("Invalid Dormitory Room Type ID: " + dormitorySettingTermReq.getDormitoryRoomTypeId());
                                }
                                return DormitorySettingTerm.builder()
                                        .dormitoryApplicationSetting(dormitoryApplicationSetting)
                                        .dormitoryRoomType(dormitoryRoomType)
                                        .acceptLimit(dormitorySettingTermReq.getAcceptLimit())
                                        .build();
                            });
                })
                .collect(Collectors.toList());

        List<DormitorySettingTerm> removeDormitorySettingTermList = findDormitorySettingTermList.stream()
                .filter(findDormitorySettingTerm -> updatedDormitorySettingTermList.stream()
                        .noneMatch(updatedDormitorySettingTerm -> updatedDormitorySettingTerm.getId().equals(findDormitorySettingTerm.getId())))
                .collect(Collectors.toList());

        dormitorySettingTermRepository.deleteAll(removeDormitorySettingTermList);
        dormitorySettingTermRepository.saveAll(updatedDormitorySettingTermList);


        //
        List<ModifyTermReq> modifyTermReqList = modifyDormitoryApplicationSettingReq.getModifyTermReqList();
        for (ModifyTermReq modifyTermReq : modifyTermReqList) {
            Term term = termRepository.findById(modifyTermReq.getTermId())
                    .orElseThrow(IllegalArgumentException::new);
            List<DormitoryTerm> findDormitoryTermList = dormitoryTermRepository.findByTerm(term);
            List<DormitoryTerm> updatedDormitoryTermList = modifyTermReq.getModifyDormitoryTermReqList().stream()
                    .map(dormitoryTermReq -> {
                        return findDormitoryTermList.stream()
                                .filter(findDormitoryTerm -> findDormitoryTerm.getId().equals(dormitoryTermReq.getDormitoryTermId()))
                                .findFirst()
                                .map(findDormitoryTerm -> {
                                    // 존재하면 업데이트
                                    findDormitoryTerm.updatePrice(dormitoryTermReq.getPrice());
                                    return findDormitoryTerm;
                                })
                                .orElseGet(() -> {
                                    // 존재하지 않으면 새로운 DormitoryTerm 생성
                                    DormitoryRoomType dormitoryRoomType = dormitoryRoomTypeRepository.findById(dormitoryTermReq.getDormitoryRoomTypeId())
                                            .orElseThrow(IllegalArgumentException::new);
                                    return DormitoryTerm.builder()
                                            .term(term)
                                            .dormitoryRoomType(dormitoryRoomType)
                                            .price(dormitoryTermReq.getPrice())
                                            .build();
                                });
                    })
                    .collect(Collectors.toList());

            // 삭제할 DormitoryTerm 목록 추출
            List<DormitoryTerm> removeDormitoryTermList = findDormitoryTermList.stream()
                    .filter(findDormitoryTerm -> updatedDormitoryTermList.stream()
                            .noneMatch(updatedDormitoryTerm -> updatedDormitoryTerm.getId().equals(findDormitoryTerm.getId())))
                    .collect(Collectors.toList());

            dormitoryTermRepository.deleteAll(removeDormitoryTermList);
            dormitoryTermRepository.saveAll(updatedDormitoryTermList);
        }


        //
        List<Term> findTermList = termRepository.findByDormitoryApplicationSetting(dormitoryApplicationSetting);
        List<Term> updatedTermList = modifyDormitoryApplicationSettingReq.getModifyTermReqList().stream()
                .map(termReq -> {
                    return findTermList.stream()
                            .filter(findTerm -> findTerm.getId().equals(termReq.getTermId()))
                            .findFirst()
                            .map(findTerm -> {
                                // 존재하면 업데이트
                                findTerm.updateTerm(termReq.getTermName(), termReq.getStartDate(), termReq.getEndDate());
                                return findTerm;
                            })
                            .orElseGet(() -> {
                                // 존재하지 않으면 새로운 Term 생성
                                return Term.builder()
                                        .dormitoryApplicationSetting(dormitoryApplicationSetting)
                                        .termName(termReq.getTermName())
                                        .startDate(termReq.getStartDate())
                                        .endDate(termReq.getEndDate())
                                        .build();
                            });
                })
                .collect(Collectors.toList());

        // 삭제할 Term 목록 추출
        List<Term> removeTermList = findTermList.stream()
                .filter(findTerm -> updatedTermList.stream()
                        .noneMatch(updatedTerm -> updatedTerm.getId().equals(findTerm.getId())))
                .collect(Collectors.toList());

        termRepository.deleteAll(removeTermList);
        termRepository.saveAll(updatedTermList);
    }

    // Description : 이전 작성 목록 조회
    public ResponseEntity<?> findDormitoryApplicationSettingHistory(UserDetailsImpl userDetailsImpl) {

        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        List<DormitoryApplicationSetting> dormitoryApplicationSettingList = dormitoryApplicationSettingRepository.findTop3BySchoolAndApplicationStatusOrderByCreatedDateDesc(school, ApplicationStatus.BEFORE);

        List<FindDormitoryApplicationSettingHistoryRes> findDormitoryApplicationSettingHistoryResList = new ArrayList<>();
        for (DormitoryApplicationSetting dormitoryApplicationSetting : dormitoryApplicationSettingList) {
            FindDormitoryApplicationSettingHistoryRes findDormitoryApplicationSettingHistoryRes = FindDormitoryApplicationSettingHistoryRes.builder()
                    .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                    .title(dormitoryApplicationSetting.getTitle())
                    .startDate(dormitoryApplicationSetting.getStartDate())
                    .endDate(dormitoryApplicationSetting.getEndDate())
                    .build();
            findDormitoryApplicationSettingHistoryResList.add(findDormitoryApplicationSettingHistoryRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(findDormitoryApplicationSettingHistoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 입사 신청 설정 프로세스 中 기숙사(인실/성별) 목록 조회
    public ResponseEntity<?> findDormitories(UserDetailsImpl userDetailsImpl) {
        User admin = userService.validateUserById(userDetailsImpl.getUserId());
        School school = admin.getSchool();

        List<Dormitory> findDormitoryList = dormitoryRepository.findBySchool(school);
        List<DormitoryRoomTypeRes> dormitoryRoomTypeResList = new ArrayList<>();
        for (Dormitory dormitory : findDormitoryList) {
            Integer dormitorySize = dormitory.getDormitorySize();
            List<DormitoryRoomType> findDormitoryRoomTypeList = dormitoryRoomTypeRepository.findByDormitory(dormitory);
            for (DormitoryRoomType dormitoryRoomType : findDormitoryRoomTypeList) {
                RoomType roomType = dormitoryRoomType.getRoomType();
                DormitoryRoomTypeRes dormitoryRoomTypeRes = DormitoryRoomTypeRes.builder()
                        .dormitoryRoomTypeId(dormitoryRoomType.getId())
                        .dormitoryName(dormitory.getName())
                        .roomSize(roomType.getRoomSize())
                        .gender(roomType.getGender())
                        .dormitorySize(dormitorySize)
                        .build();
                dormitoryRoomTypeResList.add(dormitoryRoomTypeRes);
            }
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryRoomTypeResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // Description : 이전 입사 신청 설정 목록 (신청자 명단 프로세스)
    public ResponseEntity<?> findDormitoryApplicationSettings(UserDetailsImpl userDetailsImpl) {
        User adminUser = userService.validateUserById(userDetailsImpl.getUserId());
        School school = adminUser.getSchool();

        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        applicationStatusList.add(ApplicationStatus.READY);
        applicationStatusList.add(ApplicationStatus.NOW);
        List<DormitoryApplicationSetting> dormitoryApplicationSettingList = dormitoryApplicationSettingRepository.findAllBySchoolAndApplicationStatusNotIn(school, applicationStatusList);
        List<DormitoryApplicationSettingSimpleRes> dormitoryApplicationSettingSimpleResList = new ArrayList<>();
        for (DormitoryApplicationSetting dormitoryApplicationSetting : dormitoryApplicationSettingList) {
            DormitoryApplicationSettingSimpleRes dormitoryApplicationSettingSimpleRes = DormitoryApplicationSettingSimpleRes.builder()
                    .dormitoryApplicationSettingId(dormitoryApplicationSetting.getId())
                    .title(dormitoryApplicationSetting.getTitle())
                    .createdDate(dormitoryApplicationSetting.getCreatedDate().toLocalDate())
                    .build();
            dormitoryApplicationSettingSimpleResList.add(dormitoryApplicationSettingSimpleRes);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryApplicationSettingSimpleResList)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // Description : 유효성 검증 함수
    public DormitoryApplicationSetting validateDormitoryApplicationSettingById(Long dormitoryApplicationSettingId) {
        Optional<DormitoryApplicationSetting> findDormitoryApplicationSetting = dormitoryApplicationSettingRepository.findById(dormitoryApplicationSettingId);
        DefaultAssert.isTrue(findDormitoryApplicationSetting.isPresent(), "잘못된 입사 신청 설정 정보입니다.");
        return findDormitoryApplicationSetting.get();
    }
}
