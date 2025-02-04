package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.StreaksDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dto.WorkoutRequest;
import in.ongrid.fitnesstracker.model.entities.Streaks;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WorkoutsService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutsService.class);

    private final WorkoutsDao workoutsDao;
    private final UsersDao usersDao;
    private final StreaksDao streaksDao;

    @PersistenceContext  // ✅ Inject EntityManager
    private EntityManager entityManager;

    public WorkoutsService(WorkoutsDao workoutsDao, UsersDao usersDao, StreaksDao streaksDao) {
        this.workoutsDao = workoutsDao;
        this.usersDao = usersDao;
        this.streaksDao = streaksDao;
    }

    // ✅ Get all workouts for a user by email
    public List<Workouts> getWorkoutsByUserEmail(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return workoutsDao.getWorkoutsByUser(user);
    }

    // ✅ Create a new workout and automatically update streak
    @Transactional
    public Workouts createWorkout(WorkoutRequest workoutRequest, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // ✅ Use user-provided date, or default to today's date
        LocalDate workoutDate = (workoutRequest.getDate() != null) ? workoutRequest.getDate() : LocalDate.now();

        logger.info("📅 User Requested Workout Date (or defaulted to today): {}", workoutDate);

        Workouts workout = new Workouts();
        workout.setUser(user);
        workout.setDate(workoutDate);

        // ✅ Save the workout before updating streaks
        Workouts savedWorkout = workoutsDao.saveWorkout(workout);
        entityManager.flush(); // Ensures workout is committed before checking last workout date

        logger.info("🚀 Workout Created for {} on {}", userEmail, savedWorkout.getDate());

        // ✅ Update streak based on last workout (ignoring today's date)
        updateUserStreak(user, savedWorkout.getDate());

        return savedWorkout;
    }

    // ✅ Delete a workout (Ensures only owner can delete)
    @Transactional
    public void deleteWorkout(Long workoutId, String userEmail) {
        Workouts workout = workoutsDao.getWorkoutById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found!"));

        if (!workout.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own workouts!");
        }

        workoutsDao.deleteWorkout(workoutId);
    }

    // ✅ Automatically updates the streak when a workout is created
    // ✅ Automatically updates the streak when a workout is created
    private void updateUserStreak(User user, LocalDate workoutDate) {
        Optional<LocalDate> lastWorkoutDateOpt = workoutsDao.getLastWorkoutDate(user.getUserId());
        LocalDate lastWorkoutDate = lastWorkoutDateOpt.orElse(null);

        logger.info("📅 Last Workout Date from DB: {}", lastWorkoutDate);
        logger.info("📅 New Workout Date: {}", workoutDate);

        // Fetch or create streak record
        Optional<Streaks> existingStreakOpt = streaksDao.getStreakByUserId(user.getUserId());
        Streaks streak = existingStreakOpt.orElse(new Streaks(null, user, workoutDate, 0));

        logger.info("🔥 Existing Streak Count: {}, Start Date: {}", streak.getStreakCount(), streak.getStartDate());

        // First workout ever
        if (lastWorkoutDate == null) {
            logger.info("🚀 First Workout Logged! Initializing Streak at 1.");
            streak.setStreakCount(1);
            streak.setStartDate(workoutDate);
        }
        // If last workout was exactly the previous day, increment streak
        else if (lastWorkoutDate.plusDays(1).isEqual(workoutDate)) {
            logger.info("🔥 Consecutive Workout! Increasing Streak Count.");
            streak.setStreakCount(streak.getStreakCount() + 1);
        }
        // If a day was missed, reset streak to 1
        else if (lastWorkoutDate.isBefore(workoutDate.minusDays(1))) {
            logger.info("❌ Streak Broken! Resetting to 1.");
            streak.setStreakCount(1);
            streak.setStartDate(workoutDate);
        }

        // Save updated streak and flush to ensure persistence
        streaksDao.saveStreak(streak);
        logger.info("✅ Final Streak Count: {}, Start Date: {}", streak.getStreakCount(), streak.getStartDate());

        // Force transaction to persist
        entityManager.flush();
    }

}
