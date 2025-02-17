package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Streaks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@Transactional
public class StreaksDaoImplementation implements StreaksDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Streaks> getStreakByUserId(Long userId) {
        try {
            return entityManager.createQuery(
                            "SELECT s FROM Streaks s WHERE s.user.userId = :userId", Streaks.class)
                    .setParameter("userId", userId)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty(); // Handle no streak found case
        }
    }

    @Override
    public Streaks saveOrUpdateStreak(Streaks streak) {
        if (streak.getStreakId() == null) {
            entityManager.persist(streak); // New entry
            return streak;
        } else {
            return entityManager.merge(streak); // Update existing streak
        }
    }
}
