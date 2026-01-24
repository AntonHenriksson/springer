package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.repo.UserRepository;
import se.jensen.anton.springer.security.SecurityUtils;

@Component("userAuth")
public class UserAuth {

    private final UserRepository userRepository;

    public UserAuth(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
