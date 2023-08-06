package de.hsrm.mi.sandrakiefer.hsdashboard.backend.security.jwt;

import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for jwt token, that implements generation and validation of jwt tokens.
 */
@Component
public class JwtTokenProvider {
    
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * Generates token for authenticated user.
     *
     * @param authentication auth object that contains login info
     * @return jwt token as string
     */
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        Instant now = Instant.now();
        Instant expiration = now.plus(7, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Extracts usermail from token.
     *
     * @param token jwt token as string
     * @return email of the user in the token
     */
    public String getUserMailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * Checks the validity of the token.
     * 
     * @param token jwt token as string
     * @return true if the token is valid, otherwise false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
