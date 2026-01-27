package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.security.MyUserDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * TokenService for generating JSON Web Tokens (JWT) for authenticated users.
 * It creates a signed JWT based on the provided {@link Authentication} object
 */
@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final JwtEncoder jwtEncoder;

    /**
     * This method constructs a TokenService with the give {@link JwtEncoder}
     *
     * @param jwtEncoder {@link JwtEncoder} used to encode and sign JWTs
     */
    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }


    /**
     * This method generates a signed JWT for the given authenticated user
     *
     * @param authentication {@link Authentication} object, provided by Spring Security, representing the authenticated user
     * @return JWT as {@link String}
     * @throws RuntimeException if token generation fails
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope);

        Object principal = authentication.getPrincipal();
        if (principal instanceof MyUserDetails userDetails && userDetails.getId() != null) {
            builder.claim("userId", userDetails.getId());
        }

        JwtClaimsSet claims = builder.build();

        try {
            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (RuntimeException e) {
            logger.error("Error generating token for user: {}", authentication.getName(), e);
            throw e;
        }
    }
}
