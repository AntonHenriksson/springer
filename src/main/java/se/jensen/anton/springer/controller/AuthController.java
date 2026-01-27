package se.jensen.anton.springer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import se.jensen.anton.springer.dto.LoginRequestDTO;
import se.jensen.anton.springer.dto.LoginResponseDTO;
import se.jensen.anton.springer.security.MyUserDetails;
import se.jensen.anton.springer.service.TokenService;

import java.util.Collections;

@RestController
@RequestMapping("/request-token")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> token(@RequestBody LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );
        MyUserDetails details = (MyUserDetails) auth.getPrincipal();

        String token = tokenService.generateToken(auth);
        String refreshToken = tokenService.generateRefreshToken(auth);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponseDTO(token, details.getId()));

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@CookieValue("refreshToken") String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Jwt decoded = tokenService.decodeToken(refreshToken);

            String type = decoded.getClaim("type");
            if (!"refresh".equals(type)) {
                return ResponseEntity.badRequest().build();
            }

            String username = decoded.getSubject();
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

            String newAccessToken = tokenService.generateToken(auth);

            return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
