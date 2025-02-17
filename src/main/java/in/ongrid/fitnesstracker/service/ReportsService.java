package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ReportsDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ReportRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
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
        // Validate period input
        validatePeriod(period);

        // Fetch user
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate start and end dates based on the period
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(period, endDate);

        // Fetch workouts within the time range (Ensure fresh data)
        reportsDao.clearPersistenceContext();
        List<WorkoutExercises> workoutExercises = reportsDao.getWorkoutSummary(user.getUserId(), startDate, endDate);

        // If no workouts found, return an empty report
        if (workoutExercises.isEmpty()) {
            return new ReportRequest(period, 0, 0, "No Data", Map.of(), Map.of());
        }

        // Compute metrics
        int totalWorkouts = calculateTotalWorkouts(workoutExercises);
        double totalCaloriesBurned = calculateTotalCaloriesBurned(workoutExercises);
        Map<String, Double> dailyCalories = calculateDailyCalories(workoutExercises);
        Map<String, Long> bodyPartFrequency = calculateBodyPartFrequency(workoutExercises);
        String mostTrainedBodyPart = findMostTrainedBodyPart(bodyPartFrequency);

        // Return the report
        return new ReportRequest(period, totalWorkouts, totalCaloriesBurned, mostTrainedBodyPart, bodyPartFrequency, dailyCalories);
    }

    private void validatePeriod(String period) {
        if (!List.of("weekly", "monthly", "yearly").contains(period.toLowerCase())) {
            throw new IllegalArgumentException("Invalid period. Allowed: weekly, monthly, yearly.");
        }
    }

    private LocalDate calculateStartDate(String period, LocalDate endDate) {
        switch (period.toLowerCase()) {
            case "weekly":
                return endDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case "monthly":
                return endDate.with(TemporalAdjusters.firstDayOfMonth());
            case "yearly":
                return endDate.with(TemporalAdjusters.firstDayOfYear());
            default:
                throw new IllegalArgumentException("Invalid period. Allowed: weekly, monthly, yearly.");
        }
    }

    private int calculateTotalWorkouts(List<WorkoutExercises> workoutExercises) {
        return (int) workoutExercises.stream()
                .map(we -> we.getWorkout().getWorkoutId())
                .distinct()
                .count();
    }

    private double calculateTotalCaloriesBurned(List<WorkoutExercises> workoutExercises) {
        return workoutExercises.stream()
                .mapToDouble(we -> we.getExercise().getCaloriesBurntPerRep() * we.getSets() * we.getReps())
                .sum();
    }

    private Map<String, Double> calculateDailyCalories(List<WorkoutExercises> workoutExercises) {
        return workoutExercises.stream()
                .collect(Collectors.groupingBy(
                        we -> we.getWorkout().getDate().toString(),
                        Collectors.summingDouble(we -> we.getExercise().getCaloriesBurntPerRep() * we.getSets() * we.getReps())
                ));
    }

    private Map<String, Long> calculateBodyPartFrequency(List<WorkoutExercises> workoutExercises) {
        return workoutExercises.stream()
                .collect(Collectors.groupingBy(
                        we -> we.getExercise().getBodyPart().toString(),
                        Collectors.summingLong(we -> (long) we.getSets())
                ));
    }

    private String findMostTrainedBodyPart(Map<String, Long> bodyPartFrequency) {
        return bodyPartFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Data");
    }
}