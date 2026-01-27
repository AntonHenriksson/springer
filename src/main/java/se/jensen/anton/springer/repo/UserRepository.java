package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.jensen.anton.springer.model.User;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * This repository extends {@link JpaRepository} to provide CRUD-methods.
 * It includes methods to verify post ownership and get posts created by a specific user.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Get a user along with posts by the given user ID
     *
     * @param id ID of the user to find
     * @return {@link Optional} containing {@link User} together with the user's posts  or an empty {@link Optional} if no applicable user exists.
     */
    @Query("Select u from User u LEFT JOIN FETCH u.posts WHERE u.id = :id")
    Optional<User> findUserWithPosts(Long id);

    /**
     * Find a specific user by the given username
     *
     * @param username the username of the user to find
     * @return {@link Optional} containing {@link User} or an empty {@link Optional} if no applicable user
     */
    Optional<User> findByUsername(String username);
}