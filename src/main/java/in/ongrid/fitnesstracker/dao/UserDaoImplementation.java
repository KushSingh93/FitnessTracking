package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImplementation implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Users> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM Users u", Users.class).getResultList();
    }

    @Override
    public Optional<Users> getUserById(Long userId) {
        return Optional.ofNullable(entityManager.find(Users.class, userId));
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        List<Users> users = entityManager.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Users saveUser(Users user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        Users user = entityManager.find(Users.class, userId);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
