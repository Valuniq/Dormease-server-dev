package dormease.dormeasedev.domain.dormitory.service;

import dormease.dormeasedev.domain.dormitory.domain.Dormitory;
import dormease.dormeasedev.domain.dormitory.domain.repository.DormitoryRepository;
import dormease.dormeasedev.domain.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitory.dto.response.DormitoryManagementRes;
import dormease.dormeasedev.domain.dormitory.dto.response.FloorByDormitoryRes;
import dormease.dormeasedev.domain.dormitory.dto.response.RoomByDormitoryAndFloorRes;
import dormease.dormeasedev.domain.room.domain.Room;
import dormease.dormeasedev.domain.room.domain.repository.RoomRepository;
import dormease.dormeasedev.domain.user.domain.User;
import dormease.dormeasedev.domain.user.domain.repository.UserRepository;
import dormease.dormeasedev.global.DefaultAssert;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ApiResponse;
import dormease.dormeasedev.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DormitoryManagementService {

    private final DormitoryRepository dormitoryRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    // 건물, 층별 호실 목록 조회
    public ResponseEntity<?> getRoomsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId, Integer floor) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Dormitory> dormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getRoomSize());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        List<RoomByDormitoryAndFloorRes> roomByDormitoryAndFloorRes = new ArrayList<>();
        for (Dormitory findDormitory : dormitories) {
            List<Room> rooms = roomRepository.findByDormitoryAndFloorAndIsActivated(findDormitory, floor, true);

            roomByDormitoryAndFloorRes.addAll(
                    rooms.stream()
                            .map(room -> RoomByDormitoryAndFloorRes.builder()
                                    .id(room.getId())
                                    .roomNumber(room.getRoomNumber())
                                    .roomSize(room.getRoomSize())
                                    .gender(room.getGender().toString())
                                    .currentPeople(room.getCurrentPeople())
                                    .build())
                            .toList());
        }

        // 호실 번호로 오름차순 정렬
        roomByDormitoryAndFloorRes.sort(Comparator.comparingInt(RoomByDormitoryAndFloorRes::getRoomNumber));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomByDormitoryAndFloorRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물 정보 조회

    // 학교별 건물 목록 조회(건물명(n인실))
    public ResponseEntity<?> getDormitoriesByRoomSize(CustomUserDetails customUserDetails) {
        User user = validateUserById(customUserDetails.getId());

        List<Dormitory> dormitories = dormitoryRepository.findBySchool(user.getSchool());
        DefaultAssert.isTrue(!dormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Set<String> existingDormitoryNames = new HashSet<>();
        List<DormitoryManagementRes> dormitoryManagementRes = new ArrayList<>();

        for (Dormitory dormitory : dormitories) {
            String key = dormitory.getName() + "(" + dormitory.getRoomSize() + "인실)";
            if (existingDormitoryNames.add(key)) {
                dormitoryManagementRes.add(
                        DormitoryManagementRes.builder()
                        .id(dormitory.getId())
                        .name(key).build());
            }
        }

        // 건물명 오름차순 정렬
        Comparator<DormitoryManagementRes> comparator = Comparator.comparing(DormitoryManagementRes::getName);
        dormitoryManagementRes.sort(comparator);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 건물별 층 수 목록 조회
    public ResponseEntity<?> getFloorsByDormitory(CustomUserDetails customUserDetails, Long dormitoryId) {
        Dormitory dormitory = validDormitoryById(dormitoryId);

        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getRoomSize());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        Set<Integer> uniqueFloorNumbers = new HashSet<>();

        for (Dormitory findDormitory : sameNameDormitories) {
            List<Integer> floorNumbers = roomRepository.findByDormitoryAndIsActivated(findDormitory, true).stream()
                    .map(Room::getFloor)
                    .distinct()
                    .sorted()
                    .toList();

            uniqueFloorNumbers.addAll(floorNumbers); // 중복 제거를 위해 Set에 층 수 추가
        }

        List<FloorByDormitoryRes> floorByDormitoryResList = uniqueFloorNumbers.stream()
                .map(floor -> FloorByDormitoryRes.builder()
                        .floor(floor)
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(floorByDormitoryResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // 호실 배정이 안된 사생 목록 조회

    // 수기 방배정

    // 건물별 메모 저장
    @Transactional
    public ResponseEntity<?> registerDormitoryMemo(CustomUserDetails customUserDetails, Long dormitoryId, DormitoryMemoReq dormitoryMemoReq) {
        Dormitory dormitory = validDormitoryById(dormitoryId);
        // 같은 이름의 같은 인실인 기숙사만 가져오기
        List<Dormitory> sameNameDormitories = dormitoryRepository.findBySchoolAndNameAndRoomSize(dormitory.getSchool(), dormitory.getName(), dormitory.getRoomSize());
        DefaultAssert.isTrue(!sameNameDormitories.isEmpty(), "해당 건물명의 건물이 존재하지 않습니다.");

        for (Dormitory updateDormitory : sameNameDormitories) {
            updateDormitory.updateMemo(dormitoryMemoReq.getMemo());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("메모가 저장되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    private Dormitory validDormitoryById(Long dormitoryId) {
        Optional<Dormitory> findDormitory = dormitoryRepository.findById(dormitoryId);
        DefaultAssert.isTrue(findDormitory.isPresent(), "건물 정보가 올바르지 않습니다.");
        return findDormitory.get();
    }

    public User validateUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "유저 정보가 올바르지 않습니다.");
        return findUser.get();
    }

    // 수용인원 currentPeople 더한거 / dormitorySize 더한거
    // 방개수: 꽉찬방/방 개수
    // 둘 다 활성화된 방 기준
    // 수기 배정 시 리스트로 받고,  currentPeople 업데이트하고 인원초과 시 예외처리
    // 목록에 추가한 순서대로 침대번호 배정
    // 이미 존재하는지, 존재하는 사생의 마지막 침대번호 +1인데 roomSize보단 작게


}
