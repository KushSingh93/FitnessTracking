package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class WorkoutExercisesDaoImplementation implements WorkoutExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ Retrieve all workout exercises
    @Override
    public List<WorkoutExercises> getAllWorkoutExercises() {
        return entityManager.createQuery("SELECT we FROM WorkoutExercises we", WorkoutExercises.class)
                .getResultList();
    }

    // ✅ Retrieve a specific workout exercise by ID
    @Override
    public Optional<WorkoutExercises> getWorkoutExerciseById(Long workoutExerciseId) {
        return Optional.ofNullable(entityManager.find(WorkoutExercises.class, workoutExerciseId));
    }

    // ✅ Retrieve all exercises in a given workout
    @Override
    public List<WorkoutExercises> getWorkoutExercisesByWorkout(Long workoutId) {
        return entityManager.createQuery(
                        "SELECT we FROM WorkoutExercises we WHERE we.workout.workoutId = :workoutId", WorkoutExercises.class)
                .setParameter("workoutId", workoutId)
                .getResultList();
    }

    // ✅ Save a new workout exercise (Add exercise to workout)
    @Override
    public WorkoutExercises saveWorkoutExercise(WorkoutExercises workoutExercise) {
        entityManager.persist(workoutExercise);
        return workoutExercise;
    }

    // ✅ Delete a workout exercise by ID
    @Override
    public void deleteWorkoutExercise(Long workoutExerciseId) {
        WorkoutExercises workoutExercise = entityManager.find(WorkoutExercises.class, workoutExerciseId);
        if (workoutExercise != null) {
            workoutExercise.setDeleted(true); // Mark as deleted
            entityManager.merge(workoutExercise); // Save change
        }
    }
}
