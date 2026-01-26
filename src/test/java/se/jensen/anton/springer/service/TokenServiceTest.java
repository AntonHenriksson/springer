package se.jensen.anton.springer.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class tests the {@link TokenService} class
 * Generates a key pair and uses it to create a JwtEncoder and JwtDecoder
 * The JwtEncoder and JwtDecoder are used to create a Jwt
 * The Jwt is then asserted to have the correct claim
 */

class TokenServiceTest {

    @Test
    void generateToken_createsValidJwtWithCorrectClaims() throws Exception {

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        JwtEncoder encoder = jwtEncoder(keyPair);
        JwtDecoder decoder = jwtDecoder(keyPair);

        TokenService tokenService = new TokenService(encoder);

        var auth = new UsernamePasswordAuthenticationToken(
                "userADMIN",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );

        String token = tokenService.generateToken(auth);
        Jwt jwt = decoder.decode(token);

        assertThat(jwt.getSubject()).isEqualTo("userADMIN");
        assertThat(jwt.getClaimAsString("scope")).isEqualTo("ADMIN");
        assertThat(jwt.getClaimAsString("iss")).isEqualTo("self");

        Instant now = Instant.now();
        assertThat(jwt.getExpiresAt()).isAfter(now);
    }


    private JwtEncoder jwtEncoder(KeyPair keyPair) {
        var rsa = new com.nimbusds.jose.jwk.RSAKey.Builder(
                (RSAPublicKey) keyPair.getPublic()
        ).privateKey(keyPair.getPrivate()).build();

        var jwkSet = new com.nimbusds.jose.jwk.JWKSet(rsa);
        var jwkSource = (com.nimbusds.jose.jwk.source.JWKSource<com.nimbusds.jose.proc.SecurityContext>)
                (selector, ctx) -> selector.select(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

    private JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey(
                (RSAPublicKey) keyPair.getPublic()
        ).build();
    }
}
