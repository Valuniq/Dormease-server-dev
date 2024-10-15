package dormease.dormeasedev.domain.users.resident.controller;

import dormease.dormeasedev.domain.users.resident.dto.request.UpdateResidentInfoReq;
import dormease.dormeasedev.domain.users.resident.service.ResidentManagementService;
import dormease.dormeasedev.global.common.ApiResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/residents")
public class ResidentWebController implements ResidentWebApi {

    private final ResidentManagementService residentManagementService;

    @Override
    @GetMapping("")
    public ResponseEntity<?> getResidents(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return residentManagementService.getResidents(userDetailsImpl, sortBy, isAscending, page - 1);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<?> searchResidentsByKeyword(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @RequestParam String keyword,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "isAscending", defaultValue = "true") Boolean isAscending,
            @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return residentManagementService.getSearchResidents(userDetailsImpl, keyword, sortBy, isAscending, page - 1);
    }


    @Override
    @GetMapping("/{residentId}")
    public ResponseEntity<?> getResidentInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId
    ) {
        return residentManagementService.getResidentDetailInfo(userDetailsImpl, residentId);
    }


    // Description: 사생 정보 수정
    @Override
    @PutMapping("/{residentId}")
    public ResponseEntity<Void> updateResidentInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId,
            @RequestPart Optional<MultipartFile> copy,
            @RequestPart Optional<MultipartFile> prioritySelectionCopy,
            @RequestPart UpdateResidentInfoReq updateResidentInfoReq
            ) throws IOException {
        residentManagementService.updateResidentInfo(userDetailsImpl, residentId, copy, prioritySelectionCopy, updateResidentInfoReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{residentId}/dormitory/{termId}")
    public ResponseEntity<?> getDormitoriesAssignResident(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId,
            @PathVariable Long termId
    ) {
        return residentManagementService.getDormitoriesByGender(userDetailsImpl, residentId, termId);
    }

    @Override
    @GetMapping("/manual/dormitoryApplicationSetting")
    public ResponseEntity<ApiResponse> getAvailableSettingAndTerm(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentManagementService.findAvailableDormitoryApplicationSettingAndTerm(userDetailsImpl))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    @GetMapping("/manual")
    public ResponseEntity<ApiResponse> getAvailableRoomAndBedNumber(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @RequestParam Long dormitoryId,
            @RequestParam Integer roomNumber
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(residentManagementService.assignedRoomAndBedNumber(userDetailsImpl, dormitoryId, roomNumber))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // @Operation(summary = "사생 건물 재배치", description = "사생 정보 수정 중 사생의 배정된 건물을 재배치합니다.")
    // @ApiResponses(value = {
    //         @ApiResponse(responseCode = "200", description = "수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
    //         @ApiResponse(responseCode = "400", description = "수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = .class))}),
    // })
    // @PatchMapping("/{residentId}/dormitory/{dormitoryId}")
    // public ResponseEntity<?> reassignedResident(
    //         @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    //         @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId,
    //         @Parameter(description = "기숙사의 id를 입력해주세요.", required = true) @PathVariable Long dormitoryId
    // ) {
    //     return residentManagementService.reassignResidentToDormitory(userDetailsImpl, residentId, dormitoryId);
    // }

    @Override
    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> deleteResident (
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId
    ) {
        return residentManagementService.deleteResident(userDetailsImpl, residentId);
    }

    @Override
    @DeleteMapping("/{residentId}/blackList")
    public ResponseEntity<?> addBlackList (
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId
    ) {
        return residentManagementService.addBlackList(userDetailsImpl, residentId);
    }

}
