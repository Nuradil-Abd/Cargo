package cargo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import cargo.entity.User;
import cargo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;



@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.security.jwt.secret_key}")
    private String secretKey;

    @Value("${app.security.jwt.expiration}")
    private Long expiration;

    private final UserRepository userRepo;

    public String createJwtToken(User user) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        String token = JWT.create()
                .withClaim("id", user.getId())
                .withClaim("name", user.getFirstName())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().getName())
                .withIssuedAt(zonedDateTime.toInstant())
                .withExpiresAt(zonedDateTime.plusSeconds(expiration).toInstant())
                .sign(getAlgorithm());

        return  token;
    }

    public User verifyJwtToken(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        String email = verify.getClaim("email").asString();
        return userRepo.getUserByEmail(email);
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

}
