package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutsDao {
    List<Workouts> getWorkoutsByUser(User user);

    Optional<Workouts> getWorkoutById(Long workoutId);

    Workouts saveWorkout(Workouts workout);

    void deleteWorkout(Long workoutId);

}
