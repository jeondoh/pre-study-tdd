package com.course.prestudy.auth.jwt.config;

import com.course.prestudy.auth.jwt.dto.GenerateToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-expiration}")
    private Long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey secretKey;

    /**
     * token 서명 비밀키 생성
     */
    @PostConstruct
    public void init() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * access, refreshToken 생성
     */
    public GenerateToken generateToken(String username, String userRole) {
        long now = System.currentTimeMillis();
        Date accessTokenExpiration = new Date(now + accessExpiration * 1000);
        Date refreshTokenExpiration = new Date(now + refreshExpiration * 1000);

        // accessToken 생성
        String accessToken = Jwts.builder()
                .subject(username)
                .claim("role", userRole)
                .issuedAt(new Date(now))
                .expiration(accessTokenExpiration)
                .signWith(secretKey)
                .compact();

        // refreshToken 생성
        String refreshToken = Jwts.builder()
                .issuedAt(new Date(now))
                .expiration(refreshTokenExpiration)
                .signWith(secretKey)
                .compact();

        log.info("Generated token: {}, id: {}", accessToken, username);

        return GenerateToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpired(accessExpiration)
                .refreshTokenExpired(refreshExpiration)
                .build();
    }

    /**
     * Authentication 추출
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        String role = claims.get("role", String.class);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{role})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * Claims 가져오기
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰 만료 여부
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}
