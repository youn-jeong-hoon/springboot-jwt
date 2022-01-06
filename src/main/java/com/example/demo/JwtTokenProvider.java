package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private static final String JWT_SECRET = "secretKey";

  // 토큰 유효시간
  private static final int JWT_EXPIRATION_MS = 604800000;

  // jwt 토큰 생성
  public static String generateToken(Authentication authentication) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

    return Jwts.builder()
        .setSubject((String) authentication.getPrincipal()) // 사용자
        .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
        .setExpiration(expiryDate) // 만료 시간 세팅
        .claim("userId", "admin")
        .claim("userName", "홍길동")
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
        .compact();
  }

  // Jwt 토큰에서 아이디 추출
  public static String getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();

    log.info("id:"+claims.getId());
    log.info("issuer:"+claims.getIssuer());
    log.info("issue:"+claims.getIssuedAt().toString());
    log.info("subject:"+claims.getSubject());
    log.info("Audience:"+claims.getAudience());
    log.info("expire:"+claims.getExpiration().toString());
    log.info("userName:"+claims.get("userName"));

    return claims.getSubject();
  }

  // Jwt 토큰 유효성 검사
  public static boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature", e);
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token", e);
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token", e);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token", e);
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty.", e);
    }
    return false;
  }

}