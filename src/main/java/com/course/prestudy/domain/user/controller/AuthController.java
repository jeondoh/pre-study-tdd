package com.course.prestudy.domain.user.controller;

import com.course.prestudy.core.config.ResponseApi;
import com.course.prestudy.domain.user.dto.LoginRequest;
import com.course.prestudy.domain.user.dto.LoginResponse;
import com.course.prestudy.domain.user.dto.SignupRequest;
import com.course.prestudy.domain.user.dto.SignupResponse;
import com.course.prestudy.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseApi<SignupResponse> signup(@Valid @RequestBody SignupRequest signUpRequest) {
        SignupResponse signup = authService.signup(signUpRequest);
        return ResponseApi.ok(signup);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseApi<LoginResponse> userLogin(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseApi.ok(loginResponse);
    }
}
