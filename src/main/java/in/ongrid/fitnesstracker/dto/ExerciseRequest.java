package in.ongrid.fitnesstracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRequest {

    @NotBlank
    private String exerciseName;

    @NotNull
    @Min(1)
    private Integer reps;

    @NotNull
    @Min(1)
    private Integer sets;
}
