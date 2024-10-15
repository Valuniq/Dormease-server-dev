package dormease.dormeasedev.domain.users.resident.controller;

import dormease.dormeasedev.domain.users.resident.dto.request.ResidentPrivateInfoReq;
import dormease.dormeasedev.domain.users.resident.dto.response.DormitoryResidentAssignmentRes;
import dormease.dormeasedev.domain.users.resident.dto.response.ResidentDetailInfoRes;
import dormease.dormeasedev.domain.users.resident.dto.response.ResidentRes;
import dormease.dormeasedev.domain.users.resident.service.ResidentManagementService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<?> updateResidentPrivateInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @PathVariable Long residentId,
            @RequestPart Optional<MultipartFile> copy,
            @RequestPart Optional<MultipartFile> prioritySelectionCopy,
            @RequestPart ResidentPrivateInfoReq residentPrivateInfoReq
    ) throws IOException {
        return residentManagementService.updateResidentPrivateInfo(userDetailsImpl, residentId, copy, prioritySelectionCopy, residentPrivateInfoReq);
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

    @Operation(summary = "사생 퇴사 처리", description = "사생 관리 프로세스 중 특정 사생을 퇴사 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "처리 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> deleteResident (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId
    ) {
        return residentManagementService.deleteResident(userDetailsImpl, residentId);
    }

    @Operation(summary = "사생 블랙리스트 처리", description = "사생 관리 프로세스 중 특정 사생을 블랙리스트 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "처리 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "처리 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/{residentId}/blackList")
    public ResponseEntity<?> addBlackList (
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            @Parameter(description = "사생의 id를 입력해주세요.", required = true) @PathVariable Long residentId
    ) {
        return residentManagementService.addBlackList(userDetailsImpl, residentId);
    }

}
