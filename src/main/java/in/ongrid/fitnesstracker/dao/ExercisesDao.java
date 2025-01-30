package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import java.util.List;
import java.util.Optional;

public interface ExercisesDao {
    List<Exercises> getAllExercises();
    Optional<Exercises> getExerciseById(Long exerciseId);
    List<Exercises> getExercisesByBodyPart(String bodyPart);
    Exercises saveExercise(Exercises exercise);
    void deleteExercise(Long exerciseId);
}
