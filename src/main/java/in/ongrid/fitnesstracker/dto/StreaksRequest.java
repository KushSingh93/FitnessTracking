package in.ongrid.fitnesstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StreaksRequest {
    private int streakCount;  // The number of consecutive days
    private LocalDate startDate;  // The date when the streak started
}
