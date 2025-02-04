package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
                        "SELECT e FROM Exercises e WHERE e.user.userId = :userId OR e.user.userId IN :adminIds", Exercises.class)
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
                        "SELECT e FROM Exercises e WHERE (e.user.userId = :userId OR e.user.userId IN :adminIds) AND e.bodyPart = :bodyPart", Exercises.class)
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
    public void deleteExercise(Long exerciseId) {
        Exercises exercise = entityManager.find(Exercises.class, exerciseId);
        if (exercise != null) {
            entityManager.remove(exercise);
        }
    }
}
