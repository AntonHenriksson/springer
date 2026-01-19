package se.jensen.anton.springer.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

public final class SecurityUtils {

    private SecurityUtils() {
    }


    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof MyUserDetails userDetails) {
            return userDetails.getUsername();
        }

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
    }


    public static boolean currentUserHasRole(String role) {
        if (role == null) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return false;
        }
        role = role.trim().toUpperCase();
        String required = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return auth.getAuthorities().stream()
                .anyMatch(a
                        -> required.equals(a.getAuthority()));
    }

    public static User requireCurrentUser(UserRepository userRepository) {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}
