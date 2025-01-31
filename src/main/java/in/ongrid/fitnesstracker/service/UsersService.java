package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersDao usersDao;

    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    public Optional<User> getUserById(Long userId) {
        return usersDao.getUserById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return usersDao.getUserByEmail(email);
    }

    public User createUser(User user) {
        usersDao.saveUser(user);
        return user;
    }

    public User updateUser(Long userId, User updatedUser) {
        return usersDao.getUserById(userId)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setWeightKg(updatedUser.getWeightKg());
                    user.setHeightCm(updatedUser.getHeightCm());
                    user.setAge(updatedUser.getAge());
                    user.setGender(updatedUser.getGender());
                    return usersDao.saveUser(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long userId) {
        usersDao.deleteUser(userId);
    }

    public boolean authenticateUser(String email, String password) {
        Optional<User> user = usersDao.getUserByEmail(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
}
