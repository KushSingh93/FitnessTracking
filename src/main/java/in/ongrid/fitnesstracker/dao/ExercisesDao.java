package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart; // Import BodyPart Enum
import java.util.List;
import java.util.Optional;

public interface ExercisesDao {
    List<Exercises> getAllExercises();
    Optional<Exercises> getExerciseById(Long exerciseId);

    // ✅ Updated method to accept BodyPart Enum
    List<Exercises> getExercisesByBodyPart(BodyPart bodyPart);

    Exercises saveExercise(Exercises exercise);
    void deleteExercise(Long exerciseId);
}
