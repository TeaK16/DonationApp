package finki.ukim.mk.diplomska.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class SecurityConstants {
    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long VALID = 86_400_000;
    public static final String SECRET = Base64.getEncoder().encodeToString(key.getEncoded());

}
