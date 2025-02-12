package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class WorkoutExercisesDaoImplementation implements WorkoutExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ Retrieve all workout exercises (Excluding soft deleted)
    @Override
    public List<WorkoutExercises> getAllWorkoutExercises() {
        return entityManager.createQuery(
                        "SELECT we FROM WorkoutExercises we WHERE we.deleted = false", WorkoutExercises.class)
                .getResultList();
    }

    // ✅ Retrieve a specific workout exercise by ID (Excluding soft deleted)
    @Override
    public Optional<WorkoutExercises> getWorkoutExerciseById(Long workoutExerciseId) {
        return entityManager.createQuery(
                        "SELECT we FROM WorkoutExercises we WHERE we.workoutExerciseId = :workoutExerciseId AND we.deleted = false",
                        WorkoutExercises.class)
                .setParameter("workoutExerciseId", workoutExerciseId)
                .getResultList()
                .stream()
                .findFirst();
    }

    // ✅ Retrieve all exercises for a user by date (Excluding soft deleted)
    @Override
    public List<WorkoutExercises> getExercisesByUserAndDate(Long userId, LocalDate date) {
        return entityManager.createQuery(
                        "SELECT we FROM WorkoutExercises we " +
                                "JOIN we.workout w " +
                                "WHERE w.user.userId = :userId AND w.date = :date AND we.deleted = false",
                        WorkoutExercises.class)
                .setParameter("userId", userId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public Workouts getWorkoutByUserAndDate(Long userId, LocalDate date) {
        try{
            return entityManager.createQuery(
                            "SELECT w FROM Workouts w WHERE w.user.userId = :userId AND w.date = :date",
                            Workouts.class)
                    .setParameter("userId", userId)
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ Retrieve all exercises in a given workout (Excluding soft deleted)
    @Override
    public List<WorkoutExercises> getWorkoutExercisesByWorkout(Long workoutId) {
        return entityManager.createQuery(
                        "SELECT we FROM WorkoutExercises we WHERE we.workout.workoutId = :workoutId AND we.deleted = false",
                        WorkoutExercises.class)
                .setParameter("workoutId", workoutId)
                .getResultList();
    }

    // ✅ Save a new workout exercise (Add exercise to workout)
    @Override
    public WorkoutExercises saveWorkoutExercise(WorkoutExercises workoutExercise) {
        entityManager.persist(workoutExercise);
        return workoutExercise;
    }

    // ✅ Soft delete a workout exercise by ID
    @Override
    public void deleteWorkoutExercise(Long workoutExerciseId) {
        WorkoutExercises workoutExercise = entityManager.find(WorkoutExercises.class, workoutExerciseId);
        if (workoutExercise != null) {
            workoutExercise.setDeleted(true); // Mark as deleted
            entityManager.merge(workoutExercise); // Save change
        }
    }
}
