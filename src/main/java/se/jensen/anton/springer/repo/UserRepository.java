package se.jensen.anton.springer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.anton.springer.model.User;

public interface UserRepository extends JpaRepository<User, Long> {


}
