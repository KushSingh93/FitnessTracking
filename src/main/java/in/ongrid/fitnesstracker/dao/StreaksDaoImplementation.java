package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Streaks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class StreaksDaoImplementation implements StreaksDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Streaks> getAllStreaks() {
        return entityManager.createQuery("SELECT s FROM Streaks s", Streaks.class).getResultList();
    }

    @Override
    public Optional<Streaks> getStreakByUserId(Long userId) {
        return entityManager.createQuery("SELECT s FROM Streaks s WHERE s.user.userId = :userId", Streaks.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Streaks saveStreak(Streaks streak) {
        entityManager.persist(streak);
        return streak;
    }

    @Override
    public void deleteStreak(Long streakId) {
        Streaks streak = entityManager.find(Streaks.class, streakId);
        if (streak != null) {
            entityManager.remove(streak);
        }
    }
}
