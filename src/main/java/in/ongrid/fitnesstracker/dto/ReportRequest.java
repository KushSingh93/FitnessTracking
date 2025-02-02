package in.ongrid.fitnesstracker.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String period;                 // weekly, monthly, yearly
    private int totalWorkouts;             // Total workouts in the period
    private double totalCaloriesBurned;    // Total calories burnt
    private String mostTrainedBodyPart;    // Most frequently trained body part
    private Map<String, Long> bodyPartFrequency; // Frequency of each body part trained
}
