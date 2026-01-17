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

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
