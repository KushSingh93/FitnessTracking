package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Workouts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class WorkoutsDaoImplementation implements WorkoutsDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Workouts> getAllWorkouts() {
        return entityManager.createQuery("SELECT w FROM Workouts w", Workouts.class).getResultList();
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
}
