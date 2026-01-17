package se.jensen.anton.springer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.anton.springer.dto.LoginRequestDTO;
import se.jensen.anton.springer.dto.LoginResponseDTO;
import se.jensen.anton.springer.service.TokenService;

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
        String token = tokenService.generateToken(auth);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
