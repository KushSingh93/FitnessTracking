package in.ongrid.fitnesstracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutExercisesRequest {

    private Long workoutExerciseId; // Auto-generated ID (for responses)

    @NotNull
    private Long workoutId; // ID of the workout (for request & response)

    @NotNull
    private Long exerciseId; // ID of the exercise (for request & response)

    private String exerciseName; // Optional, returned in responses

    @NotNull
    private Integer sets; // Number of sets (for request & response)

    @NotNull
    private Integer reps; // Number of reps (for request & response)

    public WorkoutExercisesRequest() {
    }

    public WorkoutExercisesRequest(Long workoutExerciseId, Long workoutId, Long exerciseId, String exerciseName, Integer sets, Integer reps) {
        this.workoutExerciseId = workoutExerciseId;
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
    }
}
