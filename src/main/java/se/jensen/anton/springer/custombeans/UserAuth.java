package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.security.SecurityUtils;

@Component("userAuth")
public class UserAuth {

    public UserAuth() {
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
}
