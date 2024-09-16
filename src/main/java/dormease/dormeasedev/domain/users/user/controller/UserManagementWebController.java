package dormease.dormeasedev.domain.users.user.controller;

import dormease.dormeasedev.domain.users.blacklist.dto.request.BlackListContentReq;
import dormease.dormeasedev.domain.users.user.dto.response.ActiveUserInfoRes;
import dormease.dormeasedev.domain.users.user.dto.response.BlackListUserInfoRes;
import dormease.dormeasedev.domain.users.user.dto.response.DeleteUserInfoRes;
import dormease.dormeasedev.domain.users.user.service.BlackListService;
import dormease.dormeasedev.domain.users.user.service.UserManagementService;
import dormease.dormeasedev.global.common.Message;
import dormease.dormeasedev.global.common.PageResponse;
import dormease.dormeasedev.global.config.security.token.CustomUserDetails;
import dormease.dormeasedev.global.exception.ExceptionResponse;
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

import java.util.List;

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
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("")
    public ResponseEntity<?> getActiveUsers(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "bonusPoint, minusPoint, createdDate 중 정렬 기준을 입력해주세요. 미입력 시 정렬은 생성일자 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "createdDate") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 내림차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "false") Boolean isAscending,
            @Parameter(description = "회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return userManagementService.getActiveUsers(customUserDetails, sortBy, isAscending, page - 1);
    }

    @Operation(summary = "회원 검색", description = "회원 관리 프로세스 중 회원을 학번 또는 이름으로 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ActiveUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchActiveUserByKeyword(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String keyword,
            @Parameter(description = "bonusPoint, minusPoint, createdDate 중 정렬 기준을 입력해주세요. 미입력 시 정렬은 생성일자 순으로 정렬됩니다.", required = true) @RequestParam(value = "sortBy", defaultValue = "createdDate") String sortBy,
            @Parameter(description = "오름차순/내림차순 기준을 입력해주세요. 미입력 시 기본 정렬은 내림차순으로 정렬됩니다.", required = true) @RequestParam(value = "isAscending", defaultValue = "false") Boolean isAscending,
            @Parameter(description = "회원을 페이지별로 조회합니다. **Page는 1부터 시작합니다!**", required = true) @Positive @RequestParam(value = "page", defaultValue = "1") Integer page

    ) {
        return userManagementService.getSearchActiveUsers(customUserDetails, keyword, sortBy, isAscending, page - 1);
    }

    // Description : 탈퇴 회원 관련 기능

    @Operation(summary = "탈퇴 회원 목록 조회", description = "탈퇴 회원 관리 프로세스 중 탈퇴 회원 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "0", description = "조회 성공 - dataList 구성", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DeleteUserInfoRes.class)))}),
            @ApiResponse(responseCode = "200", description = "조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
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
            @ApiResponse(responseCode = "400", description = "저장 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PutMapping("/blacklist")
    public ResponseEntity<?> registerBlackListContent(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "Schemas의 BlackListContentReq을 참고해주세요.", required = true) @Valid @RequestBody List<BlackListContentReq> blackListContentReqList
            ) {
        return blackListService.registerBlackListContent(customUserDetails, blackListContentReqList);
    }

    @Operation(summary = "블랙리스트 삭제", description = "블랙리스트 프로세스 중 블랙리스트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @DeleteMapping("/blacklist/{blackListId}")
    public ResponseEntity<?> deleteBlackList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(description = "블랙리스트 id를 입력해주세요.", required = true) @PathVariable Long blackListId
    ) {
        return blackListService.deleteBlackList(customUserDetails, blackListId);
    }

}
