package com.course.prestudy;

import com.course.prestudy.auth.jwt.config.JwtProvider;
import com.course.prestudy.auth.jwt.dto.GenerateToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtProviderTest 단위테스트")
public class JwtProviderTest {

    private JwtProvider jwtProvider;

    private final String testSecret = "test-secret-key-that-is-at-least-256-bits-long-for-hmac-sha256";
    private final Long testAccessExpiration = 1800L; // 30분
    private final Long testRefreshExpiration = 604800L; // 7일
    private final String testUsername = "testuser";
    private final String testRole = "ROLE_USER";

    @BeforeEach
    public void setUp() {
        jwtProvider = new JwtProvider();
        // @Value값 필드 적용
        ReflectionTestUtils.setField(jwtProvider, "secret", testSecret);
        ReflectionTestUtils.setField(jwtProvider, "accessExpiration", testAccessExpiration);
        ReflectionTestUtils.setField(jwtProvider, "refreshExpiration", testRefreshExpiration);
        jwtProvider.init();
    }

    @Test
    @DisplayName("토큰 생성")
    public void generateToken_success() {
        GenerateToken generateToken = jwtProvider.generateToken(testUsername, testRole);

        assertThat(generateToken).isNotNull();
        assertThat(generateToken.accessToken()).isNotBlank();
        assertThat(generateToken.refreshToken()).isNotBlank();
        assertThat(generateToken.accessTokenExpired()).isEqualTo(testAccessExpiration);
        assertThat(generateToken.refreshTokenExpired()).isEqualTo(testRefreshExpiration);
    }

    @Test
    @DisplayName("토큰에서 사용자 정보 추출")
    public void getClaimUserFromToken_success() {
        GenerateToken generateToken = jwtProvider.generateToken(testUsername, testRole);

        // private 으로 구현한 메서드가 있는데 테스트를 위해 열어줘야하나? - 아닌것같고
        // 코드를 똑같이 가져와서 여기서 재구현 해야하나?
        // 아님 이렇게 리플렉션을 써야하나
        Claims claims = (Claims) ReflectionTestUtils.invokeMethod(jwtProvider, "getClaimsFromToken", generateToken.accessToken());

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(testUsername);
        assertThat(claims.get("role", String.class)).isEqualTo(testRole);
        assertThat(claims.getIssuedAt()).isBefore(new Date());
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    public void validateToken_success() {
        GenerateToken generateToken = jwtProvider.generateToken(testUsername, testRole);
        boolean isValid = jwtProvider.validateToken(generateToken.accessToken());

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 시크릿으로 생성된 토큰 검증 실패")
    public void validateToken_inValidSecret_fail() {
        String invalidSecret = "aaaa-secret-key-that-is-at-least-256-bits-long-for-hmac-sha256";
        SecretKey invalidSecretKey = Keys.hmacShaKeyFor(invalidSecret.getBytes(StandardCharsets.UTF_8));

        String invalidToken = Jwts.builder()
                .subject(testUsername)
                .claim("role", testRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + testAccessExpiration * 1000))
                .signWith(invalidSecretKey)
                .compact();

        //boolean isValid = jwtProvider.validateToken(invalidToken);
        //assertThat(isValid).isFalse();

        // SecurityException 예외로 잡아놨는데
        assertThatThrownBy(() -> jwtProvider.validateToken(invalidToken))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패")
    public void validateToken_expired_fail() {
        SecretKey secretKey = Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8));
        String expiredToken = Jwts.builder()
                .subject(testUsername)
                .claim("role", testRole)
                .issuedAt(new Date(System.currentTimeMillis() - 2000))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(secretKey)
                .compact();

        boolean isValid = jwtProvider.validateToken(expiredToken);
        assertThat(isValid).isFalse();
    }
}
