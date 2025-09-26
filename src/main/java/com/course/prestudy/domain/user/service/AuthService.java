package com.course.prestudy.domain.user.service;

import com.course.prestudy.auth.jwt.config.JwtProvider;
import com.course.prestudy.auth.jwt.dto.GenerateToken;
import com.course.prestudy.auth.jwt.service.RefreshTokenService;
import com.course.prestudy.domain.user.dto.LoginRequest;
import com.course.prestudy.domain.user.dto.LoginResponse;
import com.course.prestudy.domain.user.dto.SignupRequest;
import com.course.prestudy.domain.user.dto.SignupResponse;
import com.course.prestudy.domain.user.entity.User;
import com.course.prestudy.domain.user.exception.AuthException;
import com.course.prestudy.domain.user.exception.UserException;
import com.course.prestudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        // 중복 사용자 확인
        if (userRepository.existsByUsername(signupRequest.username())) {
            throw new AuthException.DuplicateUsernameException(signupRequest.username());
        }

        // 일반 유저 생성
        String encodedPassword = passwordEncoder.encode(signupRequest.password());
        User user = User.createUser(signupRequest.username(), encodedPassword);
        User savedUser = userRepository.save(user);

        log.info("새 일반 유저 가입: {}", savedUser.getUsername());

        return SignupResponse.of(savedUser.getId(), savedUser.getUsername());
    }

    /**
     * 로그인
     */
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        // 인증
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequest.password())
        );
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserException.InvalidUserException::new);

        // 토큰 생성, 저장
        GenerateToken generateToken = jwtProvider.generateToken(username, user.getUserRole().getKey());
        refreshTokenService.saveRefreshToken(username, generateToken.refreshToken());

        log.info("로그인 성공: {}", username);
        return LoginResponse.of(generateToken.accessToken(), generateToken.refreshToken());
    }


}
