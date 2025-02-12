package in.ongrid.fitnesstracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutExercisesRequest {

    private Long workoutExerciseId;

    @NotNull
    private Long workoutId;

    @NotNull
    private Long exerciseId;

    private String exerciseName;

    @NotNull
    private Integer sets;

    @NotNull
    private Integer reps;

    @NotNull
    private Double caloriesBurntPerRep;

    public WorkoutExercisesRequest(Long workoutExerciseId, Long workoutId, Long exerciseId, String exerciseName, Integer sets, Integer reps, Double caloriesBurntPerRep) {
        this.workoutExerciseId = workoutExerciseId;
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.caloriesBurntPerRep = caloriesBurntPerRep;
    }
}
