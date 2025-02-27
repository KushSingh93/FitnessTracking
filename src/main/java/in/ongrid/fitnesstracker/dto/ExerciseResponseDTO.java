package in.ongrid.fitnesstracker.dto;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExerciseResponseDTO {
    private Long exerciseId;
    private String exerciseName;
    private BodyPart bodyPart;
    private Double caloriesBurntPerRep;
    private Long userId;
    private boolean Favourite;
}
