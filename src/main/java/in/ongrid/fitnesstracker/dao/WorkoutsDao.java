package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Workouts;
import java.util.List;
import java.util.Optional;

public interface WorkoutsDao {
    List<Workouts> getAllWorkouts();
    Optional<Workouts> getWorkoutById(Long workoutId);
    Workouts saveWorkout(Workouts workout);
    void deleteWorkout(Long workoutId);
}
