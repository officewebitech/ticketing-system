package os.ticketingsystem.login.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;

@Component
public class JWTUtils {


    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${ticketingSystem.jwtSecret}")
    private String jwtSecret;

    @Value("${ticketingSystem.jwtExpirationMs}")
    private int jwtExpirationMs;

    private String getSHA512(String jwt){

        String jwtToSHA512 = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            messageDigest.update(jwt.getBytes(StandardCharsets.UTF_8));
            jwtToSHA512 = String.format("%0128x", new BigInteger(1, messageDigest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jwtToSHA512;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(getSHA512(jwtSecret));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImplement userDetailsImplement = (UserDetailsImplement) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetailsImplement.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String getEmailFromJwtToken(String authenticationToken) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authenticationToken)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authenticationToken) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authenticationToken);
            return true;
        }catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }catch (ExpiredJwtException e) {
            logger.error("JWT Token is expired: {}", e.getMessage());
        }catch (UnsupportedJwtException e) {
            logger.error("JWT Token is unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
