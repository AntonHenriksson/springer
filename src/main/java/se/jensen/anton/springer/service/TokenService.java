package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    //tar emot användare
    //skapar token
    //signerar token
    //returnerar token i strängformat
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        logger.debug("Generating JWT token for user: {}", authentication.getName());

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()


                //self fungerar men inte att rekommendera
                //hade lite problem i tester så fick kolla upp
                //blir kvar tillsvidare


                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        try {
            String token = jwtEncoder.encode(JwtEncoderParameters
                            .from(claims))
                    .getTokenValue();
            logger.info("Token generated for user : {}, expires in 1 hour", authentication.getName());
            return token;
        } catch (Exception e) {
            logger.error("Error generating token for user: {}", authentication.getName());
            throw e;
        }
    }
}
