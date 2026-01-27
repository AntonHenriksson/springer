package se.jensen.anton.springer.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

/**
 * Security configuration for JWT authentication, CORS, password encoding, OAuth2 resource server, and key management.
 */
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    /**
     * This method configures the Spring Security filter chain
     *
     * @param http {@link HttpSecurity} object
     * @return {@link SecurityFilterChain} built from the provided configuration
     * @throws Exception Exception if an error occurs while building the security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers("/request-token").permitAll()
                                .requestMatchers(("/users")).permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
//när vi har frontend så ska detta läggas till här
// .cors(Customizer.withDefaults())
// anton
        //Jag har lagt den i rad 47
        return http.build();
    }

    /**
     * This method provides CORS configuration.
     * It allows access from specific FE origins and permits GET, POST, PUT, DELETE, and OPTIONS methods.
     *
     * @return {@link CorsConfigurationSource} with CORS settings applied
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",   // Vite dev
                "http://localhost:4173",   // Vite preview
                "https://dramatic-jennette-myprojects17-98866317.koyeb.app" // Våran front koyeb
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * This method provides a {@link PasswordEncoder} for hashing passwords
     *
     * @return {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method generates an RSA key pair from Base64-encoded keys defined in application properties.
     *
     * @param privateKey Base64-encoded private key
     * @param publicKey  Base64-encoded public key
     * @return {@link KeyPair} containing the RSA keys
     * @throws Exception if an error occurs while generating the key pair
     */
    @Bean
    public KeyPair keyPair(
            @Value("${jwt.private-key}") String privateKey,
            @Value("${jwt.public-key}") String publicKey
    ) throws Exception {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(privateBytes)
        );
        PublicKey pubKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(publicBytes)
        );
        return new KeyPair(pubKey, privKey);
    }

    /**
     * This method provides a JWK source for JWT.
     *
     * @param keyPair RSA key pair
     * @return {@link JWKSource} instance
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID("jwt-key-1")
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    /**
     * This method provides a JWT encoder.
     *
     * @param jwkSource JWK source
     * @return {@link JwtEncoder} instance
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * This method provides a JWT decoder.
     *
     * @param keyPair RSA key pair
     * @return {@link JwtDecoder} instance
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) keyPair.getPublic())
                .build();
    }

    /**
     * This method provides a {@link JwtAuthenticationConverter} to extract authorities from JWT.
     *
     * @return {@link JwtAuthenticationConverter} instance
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter =
                new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");
        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

    /**
     * This method provides the {@link AuthenticationManager} used by Spring Security.
     *
     * @param configuration {@link AuthenticationConfiguration} object
     * @return {@link AuthenticationManager} instance
     * @throws Exception if an error occurs while retrieving the authentication manager
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
