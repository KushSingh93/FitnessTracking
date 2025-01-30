package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
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

    @Override
    public List<Exercises> getExercisesByBodyPart(String bodyPart) {
        return entityManager.createQuery("SELECT e FROM Exercises e WHERE e.bodyPart = :bodyPart", Exercises.class)
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
