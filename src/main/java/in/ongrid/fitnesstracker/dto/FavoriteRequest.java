package in.ongrid.fitnesstracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequest {

    @NotNull(message = "Exercise ID cannot be null")
    private Long exerciseId;
}
