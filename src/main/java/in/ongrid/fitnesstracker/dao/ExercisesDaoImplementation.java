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
    public List<Exercises> getAllExercises() {
        return entityManager.createQuery("SELECT e FROM Exercises e", Exercises.class).getResultList();
    }

    @Override
    public Optional<Exercises> getExerciseById(Long exerciseId) {
        return Optional.ofNullable(entityManager.find(Exercises.class, exerciseId));
    }

    // ✅ Updated method to use BodyPart Enum

    public List<Exercises> getExercisesByBodyPart(BodyPart bodyPart) {
        return entityManager.createQuery(
                        "SELECT e FROM Exercises e WHERE e.bodyPart = :bodyPart", Exercises.class)
                .setParameter("bodyPart", bodyPart)  // Correctly passing enum as a parameter
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
