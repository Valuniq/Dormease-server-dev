package dormease.dormeasedev.domain.users.auth.controller;

import dormease.dormeasedev.domain.users.auth.dto.request.JoinReq;
import dormease.dormeasedev.domain.users.auth.dto.request.ModifyReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.users.auth.dto.response.CheckLoginIdRes;
import dormease.dormeasedev.domain.users.auth.dto.response.SignInRes;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[공용] Auth API", description = "Auth 관련 API입니다.")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Hidden
    @Operation(summary = "MSI 회원(Student) INSERT", description = "MSI 회원(Student) INSERT를 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "MSI 회원(Student) INSERT를 수행 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "MSI 회원(Student) INSERT를 수행 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @PostMapping("/join")
    ResponseEntity<?> join(
            @Parameter(description = "키를 입력해주세요.", required = true) @RequestParam(value = "key") String key,
            @Parameter(description = "Schemas의 JoinReq를 참고해주세요.", required = true) @Valid @RequestBody JoinReq joinReq
    );

    @Hidden
    @Operation(summary = "MSI 회원(Student) UPDATE", description = "MSI 회원(Student) UPDATE를 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "MSI 회원(Student) UPDATE를 수행 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "MSI 회원(Student) UPDATE를 수행 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @PostMapping("/modify")
    ResponseEntity<?> modify(
            @Parameter(description = "키를 입력해주세요.", required = true) @RequestParam(value = "key") String key,
            @Parameter(description = "Schemas의 ModifyReq를 참고해주세요.", required = true) @Valid @RequestBody ModifyReq modifyReq
    );

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "회원가입 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "회원가입 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @PostMapping("/sign-up")
    ResponseEntity<?> signUp(
            @Parameter(description = "Schemas의 SignUpReq를 참고해주세요.", required = true) @Valid @RequestBody SignUpReq signUpReq
    );

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "로그인 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "로그인 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @PostMapping("/sign-in")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> signIn(
            @Parameter(description = "Schemas의 SignInReq를 참고해주세요.", required = true) @RequestBody SignInReq signInReq
    );

    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "로그아웃 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))}),
            @ApiResponse(
                    responseCode = "400", description = "로그아웃 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}),
    })
    @PostMapping(value = "/sign-out")
    ResponseEntity<?> signout(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    );

    @Operation(summary = "로그인 아이디 중복 체크", description = "로그인 아이디가 중복인지 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "중복 체크 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckLoginIdRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "중복 체크 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))}
            ),
    })
    @GetMapping("/loginId/{loginId}")
    ResponseEntity<dormease.dormeasedev.global.common.ApiResponse> checkLoginId(
            @Parameter(description = "검사할 아이디를 입력해주세요.", required = true) @PathVariable(value = "loginId") String loginId
    );
}
