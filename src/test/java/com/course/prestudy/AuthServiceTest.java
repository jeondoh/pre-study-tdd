package com.course.prestudy;

import com.course.prestudy.auth.jwt.config.JwtProvider;
import com.course.prestudy.auth.jwt.dto.GenerateToken;
import com.course.prestudy.auth.jwt.service.RefreshTokenService;
import com.course.prestudy.domain.user.dto.*;
import com.course.prestudy.domain.user.entity.User;
import com.course.prestudy.domain.user.exception.AuthException;
import com.course.prestudy.domain.user.repository.UserRepository;
import com.course.prestudy.domain.user.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위테스트")
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenService refreshTokenService;

    private final String testUsername = "testuser";
    private final String testPassword = "testpassword";
    private final String testEncodedPassword = "testpass";

    @Test
    @DisplayName("회원가입 성공")
    public void signup_success() {
        SignupRequest request = SignupRequest.of(testUsername, testPassword);
        User savedUser = User.createUser(testUsername, testPassword);

        // 중복 사용자 확인
        when(userRepository.existsByUsername(testUsername)).thenReturn(false);
        // 비번 인코딩
        when(passwordEncoder.encode(testPassword)).thenReturn(testEncodedPassword);
        // user 저장
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // 응답
        SignupResponse response = authService.signup(request);

        // 멤버 검증
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(testUsername);

        // 검증
        verify(userRepository).existsByUsername(testUsername);
        verify(passwordEncoder).encode(testPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("중복 사용자명 회원가입 실패")
    public void signup_duplicateUsername_fail() {
        SignupRequest request = SignupRequest.of(testUsername, testPassword);

        // 이미 있으니 true 반환
        when(userRepository.existsByUsername(testUsername)).thenReturn(true);

        // 오류시 커스텀 exception class 반환하는지 확인
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(AuthException.DuplicateUsernameException.class);

        // 검증
        verify(userRepository).existsByUsername(testUsername);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 성공")
    public void login_success() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        LoginRequest request = LoginRequest.of(testUsername, testPassword);
        User user = User.createUser(testUsername, testEncodedPassword);
        GenerateToken generateToken = GenerateToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpired(1800L)
                .refreshTokenExpired(604800L)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));
        when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(user));
        when(jwtProvider.generateToken(testUsername, UserRole.USER.getKey())).thenReturn(generateToken);
        doNothing().when(refreshTokenService).saveRefreshToken(testUsername, refreshToken);

        // 응답
        LoginResponse response = authService.login(request);

        // 멤버검증
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(accessToken);
        assertThat(response.refreshToken()).isEqualTo(refreshToken);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername(testUsername);
        verify(jwtProvider).generateToken(testUsername, UserRole.USER.getKey());
        verify(refreshTokenService).saveRefreshToken(testUsername, refreshToken);
    }

    @Test
    @DisplayName("잘못된 정보 입력으로 로그인 실패")
    void login_invalidPassword_fail() {
        String password = "wrongPassword";
        LoginRequest request = LoginRequest.of(testUsername, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("정보 이상함"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        //verify(userRepository).findByUsername(anyString());
        verify(jwtProvider, never()).generateToken(anyString(), anyString());
        verify(refreshTokenService, never()).saveRefreshToken(anyString(), anyString());
    }
}
