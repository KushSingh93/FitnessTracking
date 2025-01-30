package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Users;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<Users> getAllUsers();
    Optional<Users> getUserById(Long userId);
    Optional<Users> getUserByEmail(String email);
    Users saveUser(Users user);
    void deleteUser(Long userId);
}
