package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.security.SecurityUtils;

@Component("userAuth")
public class UserAuth {

    public UserAuth() {
    }

    //för username
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

    //för id
    public boolean checkIfAuth(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (currentUserId == null || id == null) {
            return false;
        }

        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return true;
        }

        return currentUserId.equals(id);
    }


}
