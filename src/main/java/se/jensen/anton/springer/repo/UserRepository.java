package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.jensen.anton.springer.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByUsernameOrEmail(String username, String email);

    @Query("Select u from User u LEFT JOIN FETCH u.posts WHERE u.id = :id")
    public Optional<User> findUserWithPosts(Long id);
    //todo här är du

}
