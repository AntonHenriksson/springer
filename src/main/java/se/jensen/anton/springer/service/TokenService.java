package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
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
    private final JwtDecoder jwtDecoder;
    private static final long ACCESS_TOKEN_HOURS = 1;
    private static final long REFRESH_TOKEN_DAYS = 7;

    /**
     * This method constructs a TokenService with the give {@link JwtEncoder}
     *
     * @param jwtEncoder {@link JwtEncoder} used to encode and sign JWTs
     */
    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
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
                .expiresAt(now.plus(ACCESS_TOKEN_HOURS, ChronoUnit.HOURS))
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

    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("type", "refresh")
                .build();

        try {
            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (RuntimeException e) {
            logger.error("Error generating refresh token for user: {}", authentication.getName(), e);
            throw e;
        }
    }
}
