package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.dto.ExerciseResponseDTO;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ExercisesDaoImplementation implements ExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Exercises> getAllExercisesForUser(Long userId, List<Long> adminIds) {
        return entityManager.createQuery(
                        "SELECT e FROM Exercises e WHERE (e.user.userId = :userId OR e.user.userId IN :adminIds) AND e.deleted = false", Exercises.class)
                .setParameter("userId", userId)
                .setParameter("adminIds", adminIds)
                .getResultList();
    }

    @Override
    public Optional<Exercises> getExerciseById(Long exerciseId) {
        return Optional.ofNullable(entityManager.find(Exercises.class, exerciseId));
    }

    @Override
    public List<Exercises> getExercisesByBodyPart(BodyPart bodyPart, Long userId, List<Long> adminIds) {
        return entityManager.createQuery(
                        "SELECT e FROM Exercises e WHERE (e.user.userId = :userId OR e.user.userId IN :adminIds) AND e.bodyPart = :bodyPart AND e.deleted = false", Exercises.class)
                .setParameter("userId", userId)
                .setParameter("adminIds", adminIds)
                .setParameter("bodyPart", bodyPart)
                .getResultList();
    }

    @Override
    public Exercises saveExercise(Exercises exercise) {
        entityManager.persist(exercise);
        return exercise;
    }

    @Override
    public void softDeleteExercise(Long exerciseId) {
        Exercises exercise = entityManager.find(Exercises.class, exerciseId);
        if (exercise != null) {
            exercise.setDeleted(true);
            entityManager.merge(exercise);
        }
    }

    @Override
    public Optional<Exercises> getExerciseByName(String exerciseName, Long userId, List<Long> adminIds) {
        List<Exercises> results = entityManager.createQuery(
                        "SELECT e FROM Exercises e WHERE (e.user.userId = :userId OR e.user.userId IN :adminIds) AND e.exerciseName = :exerciseName AND e.deleted = false", Exercises.class)
                .setParameter("userId", userId)
                .setParameter("adminIds", adminIds)
                .setParameter("exerciseName", exerciseName)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Exercises getExerciseByName(ExerciseRequest exerciseReq , Long userId) {
        System.out.println("............................................................");
        System.out.println("This is the userId " +  userId);
        System.out.println("............................................................");
        List<Exercises> exercises = entityManager.createQuery(
                        "SELECT e FROM Exercises e WHERE e.user.id = :userId AND e.deleted = false AND e.exerciseName = :nameE", Exercises.class)
                .setParameter("userId", userId)
                .setParameter("nameE", exerciseReq.getExerciseName())
                .getResultList();

// Return the first match if found, otherwise return null (or handle accordingly)
        return exercises.isEmpty() ? null : exercises.get(0);
    }
}
