package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class WorkoutExercisesDaoImplementation implements WorkoutExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<WorkoutExercises> getAllWorkoutExercises() {
        return entityManager.createQuery("SELECT we FROM WorkoutExercises we", WorkoutExercises.class).getResultList();
    }

    @Override
    public Optional<WorkoutExercises> getWorkoutExerciseById(Long workoutExerciseId) {
        return Optional.ofNullable(entityManager.find(WorkoutExercises.class, workoutExerciseId));
    }

    @Override
    public List<WorkoutExercises> getWorkoutExercisesByWorkout(Long workoutId) {
        return entityManager.createQuery("SELECT we FROM WorkoutExercises we WHERE we.workout.workoutId = :workoutId", WorkoutExercises.class)
                .setParameter("workoutId", workoutId)
                .getResultList();
    }

    @Override
    public WorkoutExercises saveWorkoutExercise(WorkoutExercises workoutExercise) {
        entityManager.persist(workoutExercise);
        return workoutExercise;
    }

    @Override
    public void deleteWorkoutExercise(Long workoutExerciseId) {
        WorkoutExercises workoutExercise = entityManager.find(WorkoutExercises.class, workoutExerciseId);
        if (workoutExercise != null) {
            entityManager.remove(workoutExercise);
        }
    }
}
