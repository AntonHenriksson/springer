package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.UserRepository;
import se.jensen.anton.springer.security.SecurityUtils;

/**
 * Custom bean for user authorization checks.
 * This class provides methods to determine whether the currently authenticated user is authorized to access or perform actions on a specific user account, based on username or user ID.
 */
@Component("userAuth")
public class UserAuth {

    private final UserRepository userRepository;

    /**
     * This method constructs a UserAuth with the specified repository.
     *
     * @param userRepository The repository used to access user data
     */
    public UserAuth(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method checks whether the currently authenticated user is authorized to access the account with the given username.
     *
     * @param username The user's username
     * @return True: if the current user is authorized. False; if not authorized.
     */
    public boolean checkIfAuth(String username) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        if (currentUsername == null || username == null) {
            return false;
        }

        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return true;
        }

        return currentUsername.equals(username);
    }

    /**
     * This method checks whether the currently authenticated user is authorized to access the user account with the given ID.
     *
     * @param id ID of the user account.
     * @return True: if the current user is authorized. False; if not authorized.
     */
    public boolean checkIfAuth(Long id) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        if (currentUsername == null || id == null) {
            return false;
        }

        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return true;
        }

        return userRepository.findByUsername(currentUsername)
                .map(u -> u.getId().equals(id))
                .orElse(false);
    }
}
