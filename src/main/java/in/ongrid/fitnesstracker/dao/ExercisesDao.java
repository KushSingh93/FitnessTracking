package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import java.util.List;
import java.util.Optional;

public interface ExercisesDao {
    List<Exercises> getAllExercisesForUser(Long userId, List<Long> adminIds); // ✅ Fetch user-specific + admin exercises
    Optional<Exercises> getExerciseById(Long exerciseId);
    List<Exercises> getExercisesByBodyPart(BodyPart bodyPart, Long userId, List<Long> adminIds); // ✅ Filtered by body part
    Exercises saveExercise(Exercises exercise);
    void deleteExercise(Long exerciseId);
}
