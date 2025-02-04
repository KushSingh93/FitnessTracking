package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class WorkoutsDaoImplementation implements WorkoutsDao {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutsDaoImplementation.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Workouts> getWorkoutsByUser(User user) {
        return entityManager.createQuery(
                        "SELECT w FROM Workouts w WHERE w.user = :user ORDER BY w.date DESC", Workouts.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public Optional<Workouts> getWorkoutById(Long workoutId) {
        return Optional.ofNullable(entityManager.find(Workouts.class, workoutId));
    }

    @Override
    public Workouts saveWorkout(Workouts workout) {
        entityManager.persist(workout);
        return workout;
    }

    @Override
    public void deleteWorkout(Long workoutId) {
        Workouts workout = entityManager.find(Workouts.class, workoutId);
        if (workout != null) {
            entityManager.remove(workout);
        }
    }

    // ✅ Fetch last workout date for streak validation with logs
    @Override
    public Optional<LocalDate> getLastWorkoutDate(Long userId) {
        try {
            LocalDate lastWorkoutDate = entityManager.createQuery(
                            "SELECT MAX(w.date) FROM Workouts w WHERE w.user.userId = :userId AND w.date < :today",
                            LocalDate.class)
                    .setParameter("userId", userId)
                    .setParameter("today", LocalDate.now()) // Exclude today from query
                    .getSingleResult();

            return Optional.ofNullable(lastWorkoutDate);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
