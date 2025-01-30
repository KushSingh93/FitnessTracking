package in.ongrid.fitnesstracker.repository;

import in.ongrid.fitnesstracker.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
