package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.StreaksDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dto.StreaksRequest;
import in.ongrid.fitnesstracker.dto.StreaksRequest;
import in.ongrid.fitnesstracker.model.entities.Streaks;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StreaksService {

    private final StreaksDao streaksDao;
    private final UsersDao usersDao;
    private final WorkoutsDao workoutsDao;

    public StreaksService(StreaksDao streaksDao, UsersDao usersDao, WorkoutsDao workoutsDao) {
        this.streaksDao = streaksDao;
        this.usersDao = usersDao;
        this.workoutsDao = workoutsDao;
    }

    @Transactional
    public StreaksRequest getUserStreak(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // ✅ Fetch all workouts for the user (already sorted by date DESC)
        List<Workouts> workouts = workoutsDao.getWorkoutsByUser(user);

        if (workouts.isEmpty()) {
            return new StreaksRequest(0, null);
        }

        int streakCount = 1;
        LocalDate streakStartDate = workouts.get(0).getDate();

        // ✅ Check consecutive workout days to calculate streak
        for (int i = 1; i < workouts.size(); i++) {
            LocalDate prevDate = workouts.get(i - 1).getDate();
            LocalDate currDate = workouts.get(i).getDate();

            if (prevDate.minusDays(1).equals(currDate)) {
                streakCount++;
                streakStartDate = currDate;
            } else {
                break;
            }
        }

        // ✅ Fetch or create a new streak record
        Streaks streak = streaksDao.getStreakByUserId(user.getUserId())
                .orElse(new Streaks(null, user, streakStartDate, streakCount));

        // ✅ Update streak count and start date
        streak.setStreakCount(streakCount);
        streak.setStartDate(streakStartDate);
        streaksDao.saveOrUpdateStreak(streak);

        return new StreaksRequest(streakCount, streakStartDate);
    }

}
