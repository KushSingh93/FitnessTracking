package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.StreaksDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.StreaksRequest;
import in.ongrid.fitnesstracker.model.entities.Streaks;
import in.ongrid.fitnesstracker.model.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StreaksService {

    private final StreaksDao streaksDao;
    private final UsersDao usersDao;

    public StreaksService(StreaksDao streaksDao, UsersDao usersDao) {
        this.streaksDao = streaksDao;
        this.usersDao = usersDao;
    }

    // ✅ Get the user's current streak
    public StreaksRequest getUserStreak(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Optional<Streaks> streak = streaksDao.getStreakByUserId(user.getUserId());

        if (streak.isEmpty()) {
            return new StreaksRequest(0, LocalDate.now()); // No streak found, return 0
        }

        return new StreaksRequest(streak.get().getStreakCount(), streak.get().getStartDate());
    }

    // ✅ Update streak when user completes a workout
    public StreaksRequest updateUserStreak(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Optional<Streaks> existingStreak = streaksDao.getStreakByUserId(user.getUserId());

        if (existingStreak.isEmpty()) {
            // No existing streak, create a new one
            Streaks newStreak = new Streaks();
            newStreak.setUser(user);
            newStreak.setStartDate(LocalDate.now());
            newStreak.setStreakCount(1);

            streaksDao.saveStreak(newStreak);
            return new StreaksRequest(1, LocalDate.now());
        }

        Streaks streak = existingStreak.get();
        if (streak.getStartDate().isBefore(LocalDate.now())) {
            // Update streak count if user has worked out today
            streak.setStreakCount(streak.getStreakCount() + 1);
            streak.setStartDate(LocalDate.now());

            streaksDao.saveStreak(streak);
        }

        return new StreaksRequest(streak.getStreakCount(), streak.getStartDate());
    }

    // ✅ Reset streak when user misses a workout
    public void resetUserStreak(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Optional<Streaks> existingStreak = streaksDao.getStreakByUserId(user.getUserId());

        if (existingStreak.isPresent()) {
            Streaks streak = existingStreak.get();
            streak.setStreakCount(0);
            streak.setStartDate(LocalDate.now());

            streaksDao.saveStreak(streak);
        }
    }
}
