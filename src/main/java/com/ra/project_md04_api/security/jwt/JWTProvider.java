package com.ra.project_md04_api.security.jwt;

import com.ra.project_md04_api.security.principals.CustomUserDetail;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt_expired}")
    private Long EXPIRED_ACCESS;
    @Value("${secret_key}")
    private String SECRET_KEY;

    public String getAccessToken(CustomUserDetail customUserDetail) {
        return Jwts.builder()
                .setSubject(customUserDetail.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRED_ACCESS))
                .signWith(SignatureAlgorithm.ES512, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException ex) {
            log.error("Invalid JWT token {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Signature exception {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Malformed URL exception {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty {}", ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
