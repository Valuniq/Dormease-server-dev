package dormease.dormeasedev.domain.user.controller;

import dormease.dormeasedev.domain.blacklist.dto.request.BlackListContentReq;
import dormease.dormeasedev.domain.user.dto.response.ActiveUserInfoRes;
import dormease.dormeasedev.domain.user.dto.response.BlackListUserInfoRes;
import dormease.dormeasedev.domain.user.dto.response.DeleteUserInfoRes;
import dormease.dormeasedev.domain.user.service.BlackListService;
import dormease.dormeasedev.domain.user.service.UserManagementService;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.payload.ErrorResponse;
import dormease.dormeasedev.global.payload.Message;
import dormease.dormeasedev.global.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management API", description = "User Management 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/web/users/management")
public class UserManagementWebController {

    private final UserManagementService userManagementService;
    private final BlackListService blackListService;

    @Operation(summary = "회원 목록 조회", description = "회원 관리 프로세스 중 회원 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ActiveUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getActiveUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return userManagementService.getActiveUsers(customUserDetails, page - 1);
    }

    @Operation(summary = "회원 정렬", description = "회원 관리 프로세스 중 회원 목록을 입력받은 기준으로 정렬하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ActiveUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/sort")
    public ResponseEntity<?> sortedUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "bonusPoint, minusPoint, createdDate 중 정렬 기준을 입력해주세요.", required = true) @RequestParam String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요.", required = true) @RequestParam Boolean isAscending,
            @Parameter(description = "회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return userManagementService.sortedUsers(customUserDetails, sortBy, isAscending, page - 1);
    }

    @Operation(summary = "회원 검색", description = "회원 관리 프로세스 중 회원을 학번 또는 이름으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ActiveUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchActiveUserByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return userManagementService.searchActiveUsers(customUserDetails, keyword, page - 1);
    }

    // Description : 탈퇴 회원 관련 기능

    @Operation(summary = "탈퇴 회원 목록 조회", description = "탈퇴 회원 관리 프로세스 중 탈퇴 회원 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DeleteUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/delete")
    public ResponseEntity<?> getDeleteUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "탈퇴 회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return userManagementService.getDeleteUserBySchool(customUserDetails, page - 1);
    }

    @Operation(summary = "탈퇴 회원 검색", description = "탈퇴 회원 관리 프로세스 중 탈퇴 회원을 학번 또는 이름으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DeleteUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/delete/search")
    public ResponseEntity<?> searchDeleteUserByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "탈퇴 회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return userManagementService.searchDeleteUsers(customUserDetails, keyword, page - 1);
    }

    // Description : 블랙리스트 관련 기능

    @Operation(summary = "블랙리스트 목록 조회", description = "블랙리스트 프로세스 중 블랙리스트 회원 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BlackListUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/blacklist")
    public ResponseEntity<?> getBlackListUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "블랙리스트를 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return blackListService.getBlackListUsers(customUserDetails, page - 1);
    }

    @Operation(summary = "블랙리스트 사유 작성", description = "블랙리스트 프로세스 중 블랙리스트 사유를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PutMapping("/blacklist/{blackListId}")
    public ResponseEntity<?> registerBlackListContent(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "블랙리스트 id를 입력해주세요.", required = true) @PathVariable Long blackListId,
            @Parameter(description = "Schemas의 BlackListContentReq을 참고해주세요.", required = true) @Valid @RequestBody BlackListContentReq blackListContentReq
            ) {
        return blackListService.registerBlackListContent(customUserDetails, blackListId, blackListContentReq);
    }

    @Operation(summary = "블랙리스트 삭제", description = "블랙리스트 프로세스 중 블랙리스트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @DeleteMapping("/blacklist/{blackListId}")
    public ResponseEntity<?> deleteBlackList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "블랙리스트 id를 입력해주세요.", required = true) @PathVariable Long blackListId
    ) {
        return blackListService.deleteBlackList(customUserDetails, blackListId);
    }

}
