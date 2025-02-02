package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import java.time.LocalDate;
import java.util.List;

public interface ReportsDao {
    List<WorkoutExercises> getWorkoutSummary(Long userId, LocalDate startDate, LocalDate endDate);
}
