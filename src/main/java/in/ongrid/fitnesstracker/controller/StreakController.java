package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {

    @Autowired
    private StreakService streakService;

    // ✅ Get a user's streak
    @GetMapping("/{userId}")
    public ResponseEntity<Integer> getUserStreak(@PathVariable Long userId) {
        return ResponseEntity.ok(streakService.getUserStreak(userId));
    }
}
