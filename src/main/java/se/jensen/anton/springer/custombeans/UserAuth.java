package se.jensen.anton.springer.custombeans;

import org.springframework.stereotype.Component;
import se.jensen.anton.springer.security.SecurityUtils;

@Component("userAuth")
public class UserAuth {

    public UserAuth() {
    }

    public boolean checkIfAuth(Long userId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (currentUserId == null || userId == null) {
            return false;
        }

        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return true;
        }

        return currentUserId.equals(userId);
    }
}
