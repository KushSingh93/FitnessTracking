package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ReportsDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ReportRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    private final ReportsDao reportsDao;
    private final UsersDao usersDao;

    public ReportsService(ReportsDao reportsDao, UsersDao usersDao) {
        this.reportsDao = reportsDao;
        this.usersDao = usersDao;
    }

    public ReportRequest getWorkoutSummary(String userEmail, String period) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "weekly":
                startDate = endDate.minusDays(7);
                break;
            case "monthly":
                startDate = endDate.minusMonths(1);
                break;
            case "yearly":
                startDate = endDate.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid period. Allowed: weekly, monthly, yearly.");
        }

        // ✅ Fetch workouts within the time range
        List<WorkoutExercises> workoutExercises = reportsDao.getWorkoutSummary(user.getUserId(), startDate, endDate);

        // 🔹 LOGGING: Ensure data is being fetched
        System.out.println("Fetched Workouts: " + workoutExercises.size());

        if (workoutExercises.isEmpty()) {
            return new ReportRequest(period, 0, 0, "No Data", Map.of());
        }

        // ✅ Compute Total Workouts
        int totalWorkouts = (int) workoutExercises.stream()
                .map(we -> we.getWorkout().getWorkoutId()) // Get unique workout IDs
                .distinct()
                .count();

        // ✅ Compute Total Calories Burned
        double totalCaloriesBurned = workoutExercises.stream()
                .mapToDouble(we -> we.getExercise().getCaloriesBurntPerRep() * we.getSets())
                .sum();

        // ✅ Compute Body Part Trained Frequency
        Map<String, Long> bodyPartFrequency = workoutExercises.stream()
                .collect(Collectors.groupingBy(
                        we -> we.getExercise().getBodyPart().toString(), // Ensure proper conversion
                        Collectors.counting()
                ));

        // ✅ Compute Most Trained Body Part
        String mostTrainedBodyPart = bodyPartFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Data");

        // 🔹 LOGGING: Ensure calculated values
        System.out.println("Total Workouts: " + totalWorkouts);
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        System.out.println("Most Trained Body Part: " + mostTrainedBodyPart);
        System.out.println("Body Part Frequency: " + bodyPartFrequency);

        return new ReportRequest(period, totalWorkouts, totalCaloriesBurned, mostTrainedBodyPart, bodyPartFrequency);
    }
}
