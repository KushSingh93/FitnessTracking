package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import java.util.List;
import java.util.Optional;

public interface WorkoutExercisesDao {
    
    List<WorkoutExercises> getAllWorkoutExercises();
    
    Optional<WorkoutExercises> getWorkoutExerciseById(Long workoutExerciseId);
    
    List<WorkoutExercises> getWorkoutExercisesByWorkout(Long workoutId);
    
    WorkoutExercises saveWorkoutExercise(WorkoutExercises workoutExercise);
    
    void deleteWorkoutExercise(Long workoutExerciseId);
}
