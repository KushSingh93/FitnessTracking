package in.ongrid.fitnesstracker.dto;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkoutResponse {
    List<Exercises> exercises;
    LocalDate date;
}
