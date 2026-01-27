package se.jensen.anton.springer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.anton.springer.model.User;
import se.jensen.anton.springer.repo.UserRepository;
import se.jensen.anton.springer.security.MyUserDetails;


/**
 * Service that implements {@link UserDetailsService} for Spring Security.
 * This service returns a {@link MyUserDetails} object, which Spring Security uses for authentication and authorization checks.
 * {@link #loadUserByUsername(String)}} is automatically called during the authentication flow via Spring Security and does not need to be invoked manually by other classes.
 */
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method loads user info by username.
     * It fetches {@link User} entity from the database, and return it as a {@link MyUserDetails} object.
     *
     * @param username The username identifies the user whose data is to be fetched
     * @return {@link MyUserDetails} object including the user's ID, username, password, role (authorities), the original User object, and the account status flags: isEnabled(), isAccountNonExpired(), isAccountNonLocked(), and isCredentialsNonExpired().
     * @throws UsernameNotFoundException if no user with the given username exists
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        logger.debug("Finding User object by Username{}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.debug("User not found with username: {}", username);
                    return new UsernameNotFoundException(username);
                });
        logger.debug("Found object with username: {}", username);
        return new MyUserDetails(user);
    }
}
