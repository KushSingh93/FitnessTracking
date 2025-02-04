package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UsersDaoImplementation implements UsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public List<User> getAllAdmins() {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.userType = 'ADMIN'", User.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(entityManager.find(User.class, userId));
    }

    @Override
    @Transactional
    public Optional<User> getUserByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
