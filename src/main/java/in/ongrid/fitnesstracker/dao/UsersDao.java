package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UsersDao {
    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
    void deleteUser(Long userId);
}
