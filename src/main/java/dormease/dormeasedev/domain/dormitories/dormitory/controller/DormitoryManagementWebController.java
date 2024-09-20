package dormease.dormeasedev.domain.dormitories.dormitory.controller;

import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.AssignedResidentToRoomReq;
import dormease.dormeasedev.domain.dormitories.dormitory.dto.request.DormitoryMemoReq;
import dormease.dormeasedev.domain.dormitories.dormitory.service.DormitoryManagementService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/dormitory/management")
public class DormitoryManagementWebController implements DormitoryManagementWebApi {

    private final DormitoryManagementService dormitoryManagementService;

    @Override
    @PutMapping("/{dormitoryId}/memo")
    public ResponseEntity<?> registerDormitoryMemo(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long dormitoryId,
            @Valid @RequestBody DormitoryMemoReq dormitoryMemoReq
    ) {
        dormitoryManagementService.registerDormitoryMemo(dormitoryId, dormitoryMemoReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("")
    public ResponseEntity<ApiResponse> getDormitoriesByRoomSize(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getDormitoriesByRoomSize(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{dormitoryId}/floor")
    public ResponseEntity<ApiResponse> getFloorsByDormitory(
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
        @PathVariable Long dormitoryId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getFloorsByDormitory(userDetailsImpl, dormitoryId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{dormitoryId}/{floor}")
    public ResponseEntity<ApiResponse> getFloorsByDormitory(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long dormitoryId,
            @PathVariable Integer floor
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getRoomsByDormitory(userDetailsImpl, dormitoryId, floor))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/{dormitoryId}")
    public ResponseEntity<ApiResponse> getDormitoryInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long dormitoryId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getDormitoryInfo(userDetailsImpl, dormitoryId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/rooms/{roomId}/not-assigned")
    public ResponseEntity<ApiResponse> getNotAssignedResidents(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long roomId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getNotAssignedResidents(roomId))
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/rooms/{roomId}/assigned")
    public ResponseEntity<ApiResponse> getAssignedResidents(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long roomId
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(dormitoryManagementService.getAssignedResidents(userDetailsImpl, roomId))
                .build();
        return  ResponseEntity.ok(apiResponse);
    }

    @Override
    @PutMapping("/rooms/{roomId}/manual")
    public ResponseEntity<?> assignedResidentsToRooms(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long roomId,
            @Valid @RequestBody AssignedResidentToRoomReq assignedResidentToRoomReq
            ) {
        dormitoryManagementService.assignedResidentsToRoom(roomId, assignedResidentToRoomReq);
        return ResponseEntity.noContent().build();
    }
}


