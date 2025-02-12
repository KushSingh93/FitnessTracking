package in.ongrid.fitnesstracker.dto;

import in.ongrid.fitnesstracker.model.enums.BodyPart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRequest {

    @NotBlank(message = "Exercise name cannot be blank")
    private String exerciseName;

    @NotNull(message = "Body part cannot be null")
    private BodyPart bodyPart;

    @NotNull  // ✅ Ensure this is required
    private Double caloriesBurntPerRep;

}
