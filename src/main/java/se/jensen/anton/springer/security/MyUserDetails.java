package se.jensen.anton.springer.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import se.jensen.anton.springer.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Wrapper class that implements {@link UserDetails} for Spring Security.
 * This class wraps a {@link User} entity and provides the required information for authentication and authorization.
 * Spring Security uses instances of this class internally during the authentication flow, so they should not be used as API responses.
 */
public class MyUserDetails implements UserDetails {

    private final User user;

    /**
     * Constructs a MyUserDetails object wrapping the {@link User} entity
     *
     * @param user {@link User} object that will be used during the authentication flow
     */
    public MyUserDetails(User user) {
        this.user = user;
    }


    /**
     * This method returns the ID of the wrapped {@link User} entity
     *
     * @return the user's ID
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * This method returns the authorities granted to the wrapped {@link User} entity
     *
     * @return {@link Collection} of {@link GrantedAuthority} containing the user's roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        if (role == null) return List.of();
        String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(normalized));
    }


    /**
     * The method returns the hashed password of the user
     *
     * @return the hashed password of the user
     */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /**
     * This method returns the username of the user
     *
     * @return the user's username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * This method returns the wrapped {@link User} entity.
     *
     * @return the wrapped {@link User}
     */
    public User getUser() {
        return user;
    }

    /**
     * This method returns whether the user's account has not expired.
     *
     * @return True: if the account is valid. False: if the account has expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * This method returns whether the user's account has not locked.
     *
     * @return True: if the account is not locked. False: if it is locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * This method returns whether the user's credentials (password) have not expired.
     *
     * @return True: if the credentials are still valid. False: if the credentials have expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * This method returns whether the user's account is enabled.
     *
     * @return True: if the user's account it valid. False: if the user's account is disabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
