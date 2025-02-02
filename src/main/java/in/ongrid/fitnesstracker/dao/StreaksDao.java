package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.Streaks;
import java.util.Optional;

public interface StreaksDao {

    Optional<Streaks> getStreakByUserId(Long userId); // Retrieve streak for a specific user

    Streaks saveStreak(Streaks streak); // Save or update a streak record
}
