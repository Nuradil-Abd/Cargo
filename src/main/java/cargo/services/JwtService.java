package cargo.services;

import cargo.entity.Account;
import cargo.repositories.AccountRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.transaction.annotation.Transactional;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import cargo.entity.User;
import cargo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AccountRepository accountRepo;
    @Transactional
    public String createJwtToken(User user) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        Account account = accountRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return   JWT.create()
                .withClaim("id", user.getId())
                .withClaim("name", user.getFirstName())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().getName())
                .withClaim("login",account.getLogin())
                .withIssuedAt(zonedDateTime.toInstant())
                .withExpiresAt(zonedDateTime.plusSeconds(expiration).toInstant())
                .sign(getAlgorithm());

    }

    @Transactional
    public User verifyJwtToken(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String login = decodedJWT.getClaim("login").asString();
        Account account = accountRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        return account.getUser();
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

}
