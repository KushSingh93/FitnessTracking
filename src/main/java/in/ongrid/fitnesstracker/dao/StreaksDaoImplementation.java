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

    // ✅ Retrieve the user's current streak
    @Override
    public Optional<Streaks> getStreakByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT s FROM Streaks s WHERE s.user.userId = :userId", Streaks.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();
    }

    // ✅ Save or update the streak record
    @Override
    public Streaks saveStreak(Streaks streak) {
        return entityManager.merge(streak);
    }
}
