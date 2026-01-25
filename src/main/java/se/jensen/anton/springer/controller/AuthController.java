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
import se.jensen.anton.springer.security.MyUserDetails;
import se.jensen.anton.springer.service.TokenService;

/**
 * REST controller for handling user authentication requests.
 * This class provides an endpoint for users to log in and receive a JWT.
 * This controller uses {@link AuthenticationManager} to verify user credentials and {@link TokenService} to generate JWT tokens upon successful authentication.
 */
@RestController
@RequestMapping("/request-token")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * It constructs an {@code AuthController} with TokenService and AuthenticationManager
     *
     * @param tokenService          the service responsible for generating JWT tokens
     * @param authenticationManager the manager used to authenticate user credentials
     */
    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * POST-method to generate a token for existing users after successful authentication
     *
     * @param dto {@link LoginRequestDTO} which consists of username and password used for authentication
     * @return {@link ResponseEntity} containing a token and userId with HTTP 200 status
     */
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
        return ResponseEntity.ok(new LoginResponseDTO(token, details.getId()));
    }
}
