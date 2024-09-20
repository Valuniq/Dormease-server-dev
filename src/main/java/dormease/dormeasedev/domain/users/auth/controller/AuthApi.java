package dormease.dormeasedev.domain.users.auth.controller;

import dormease.dormeasedev.domain.users.auth.dto.request.SignInReq;
import dormease.dormeasedev.domain.users.auth.dto.request.SignUpReq;
import dormease.dormeasedev.domain.users.auth.dto.response.CheckLoginIdRes;
import dormease.dormeasedev.domain.users.auth.dto.response.SignInRes;
import dormease.dormeasedev.global.exception.ExceptionResponse;
import dormease.dormeasedev.global.security.UserDetailsImpl;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[공용] Auth API", description = "Auth 관련 API입니다. 현재는 경로가 app으로 되어있지만 우선순위가 높지 않으니 차례가 되면 수정하겠습니다.")
public interface AuthApi {

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
            @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody SignUpReq signUpReq
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
            @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @RequestBody SignInReq signInReq
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