package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Streaks;
import java.util.List;
import java.util.Optional;

public interface StreaksDao {
    List<Streaks> getAllStreaks();
    Optional<Streaks> getStreakByUserId(Long userId);
    Streaks saveStreak(Streaks streak);
    void deleteStreak(Long streakId);
}
