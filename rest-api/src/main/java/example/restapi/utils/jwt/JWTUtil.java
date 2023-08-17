package example.restapi.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import example.restapi.utils.Constants;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJWTToken(UUID appUserId,String username,String role)  throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(Constants.JWT_SUBJECT)
                .withClaim(Constants.JWT_CLAIM_APP_USER_ID, String.valueOf(appUserId))
                .withClaim(Constants.JWT_CLAIM_USERNAME,username)
                .withClaim(Constants.JWT_CLAIM_ROLE,role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(Date.from(LocalDateTime.now().plusMonths(6).toInstant(ZoneOffset.UTC)))
                .withIssuer(Constants.JWT_ISSUER)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String,Object> validateJWTTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(Constants.JWT_SUBJECT)
                .withIssuer(Constants.JWT_ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return new HashMap<>(){{
            put(Constants.JWT_CLAIM_APP_USER_ID,jwt.getClaim("appUserId").asString());
            put(Constants.JWT_CLAIM_USERNAME,jwt.getClaim("username").asString());
            put(Constants.JWT_CLAIM_ROLE,jwt.getClaim("role").asString());
        }};
    }

}
