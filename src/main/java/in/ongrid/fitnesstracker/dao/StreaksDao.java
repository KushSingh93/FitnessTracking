package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Streaks;
import java.util.Optional;

public interface StreaksDao {
    Optional<Streaks> getStreakByUserId(Long userId);
    Streaks saveOrUpdateStreak(Streaks streak);
}
