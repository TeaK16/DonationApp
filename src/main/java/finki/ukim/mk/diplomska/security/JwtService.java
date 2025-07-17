package finki.ukim.mk.diplomska.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {
    public  Date currentDate = new Date();
    public  Date expireDate = new Date(currentDate.getTime() + SecurityConstants.VALID);



    public JwtService() {

    }

    public String generatedToken(UserDetails userDetails, UUID userId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("jti", UUID.randomUUID().toString());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(generationKey())
                .compact();
    }
     public SecretKey generationKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SecurityConstants.SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
     }

     public String extractEmail(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getSubject();

     }

     
     public boolean isTokenValid(String jwt){
         Claims claims = getClaims(jwt);
         return claims.getExpiration().after(Date.from(Instant.now()));
     }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generationKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
