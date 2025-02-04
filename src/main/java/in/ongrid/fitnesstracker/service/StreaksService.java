package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.StreaksDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dto.StreaksRequest;
import in.ongrid.fitnesstracker.model.entities.Streaks;
import in.ongrid.fitnesstracker.model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StreaksService {

    private static final Logger logger = LoggerFactory.getLogger(StreaksService.class);

    private final StreaksDao streaksDao;
    private final UsersDao usersDao;
    private final WorkoutsDao workoutsDao;

    public StreaksService(StreaksDao streaksDao, UsersDao usersDao, WorkoutsDao workoutsDao) {
        this.streaksDao = streaksDao;
        this.usersDao = usersDao;
        this.workoutsDao = workoutsDao;
    }

    // ✅ Get and update streak automatically
    public StreaksRequest getAndUpdateUserStreak(String userEmail) {
        logger.info("Fetching streak for user: {}", userEmail);

        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", userEmail);
                    return new RuntimeException("User not found!");
                });

        // ✅ Fetch last valid workout date
        Optional<LocalDate> lastWorkoutDateOpt = workoutsDao.getLastWorkoutDate(user.getUserId());
        LocalDate lastWorkoutDate = lastWorkoutDateOpt.orElse(null);

        logger.info("📅 Last Workout Date from DB: {}", lastWorkoutDate);

        // ❌ No workout history → Reset streak to 0 with startDate = null
        if (lastWorkoutDate == null) {
            logger.info("🚀 No workouts found! Returning streak count = 0.");
            return new StreaksRequest(0, null);
        }

        // ✅ Retrieve existing streak
        Optional<Streaks> existingStreakOpt = streaksDao.getStreakByUserId(user.getUserId());
        Streaks streak = existingStreakOpt.orElse(new Streaks(null, user, lastWorkoutDate, 0));

        logger.info("🔥 Existing Streak Count: {}, Start Date: {}", streak.getStreakCount(), streak.getStartDate());

        // ✅ If first workout or streak was reset, initialize streak
        if (existingStreakOpt.isEmpty()) {
            logger.info("🚀 No existing streak found! Starting fresh streak from last workout date.");
            streak.setStreakCount(1);
            streak.setStartDate(lastWorkoutDate);
        }
        // ✅ Consecutive day workout → Increment streak
        else if (lastWorkoutDate.equals(streak.getStartDate().plusDays(streak.getStreakCount()))) {
            logger.info("🔥 Consecutive Workout! Increasing Streak Count.");
            streak.setStreakCount(streak.getStreakCount() + 1);
        }
        // ❌ Missed a day → Reset streak
        else {
            logger.info("❌ Streak Broken! Resetting to 1.");
            streak.setStreakCount(1);
            streak.setStartDate(lastWorkoutDate);
        }

        // ✅ Save streak and ensure persistence
        streaksDao.saveStreak(streak);
        logger.info("✅ Final Streak Count for {}: {}, Start Date: {}", userEmail, streak.getStreakCount(), streak.getStartDate());

        return new StreaksRequest(streak.getStreakCount(), streak.getStartDate());
    }
}
