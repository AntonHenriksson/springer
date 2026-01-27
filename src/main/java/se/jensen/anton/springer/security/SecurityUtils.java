package se.jensen.anton.springer.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;

/**
 * Class for checking the currently authenticated user's info
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }


    /**
     * This method returns the username of the currently authenticated user.
     *
     * @return The username of the current user, or null if not authenticated
     * @throws IllegalStateException if the principal type is unexpected
     */
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


    /**
     * This method returns the ID of the currently authenticated user.
     * If no user is authenticated, or the authentication represents an anonymous user, it returns null.
     *
     * @return ID of the current user, or null if not authenticated
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof MyUserDetails userDetails) {
            return userDetails.getId();
        }

        if (principal instanceof Jwt jwt) {
            Object userIdClaim = jwt.getClaims().get("userId"); // or "id"

            if (userIdClaim instanceof Number number) {
                return number.longValue();
            }
        }

        return null;
    }


    /**
     * This method checks whether the currently authenticated user has the specified role.
     *
     * @param role The role of the current user.
     * @return True: if the current user has the role. False: if not.
     */
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

    /**
     * This method fetches the current {@link User} entity from the given {@link UserRepository}.
     *
     * @param userRepository The repository used to find the user
     * @return {@link User} entity of the currently authenticated user
     * @throws ResponseStatusException if no user is authenticated or the user is not found
     */
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
