package in.ongrid.fitnesstracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class WorkoutRequest {

    @NotNull
    private LocalDate date; // Only date is needed, userId comes from JWT
}
