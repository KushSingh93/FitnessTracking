package in.ongrid.fitnesstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class StreaksRequest { // ✅ Keep this name as per your preference
    private final int streakCount;
    private final LocalDate startDate;
}
