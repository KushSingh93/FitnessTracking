package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.StreaksRequest;
import in.ongrid.fitnesstracker.service.StreaksService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
public class StreaksController {

    private final StreaksService streaksService;
    private final JwtUtil jwtUtil;

    public StreaksController(StreaksService streaksService, JwtUtil jwtUtil) {
        this.streaksService = streaksService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Get the current streak of a user
    @GetMapping("/getStreak")
    public ResponseEntity<StreaksRequest> getUserStreak(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        return ResponseEntity.ok(streaksService.getUserStreak(userEmail));
    }

    // ✅ Update the streak when a workout is completed
    @PostMapping("/update")
    public ResponseEntity<StreaksRequest> updateStreak(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        return ResponseEntity.ok(streaksService.updateUserStreak(userEmail));
    }

    // ✅ Reset the streak when a user misses a workout
    @PostMapping("/reset")
    public ResponseEntity<Void> resetStreak(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        streaksService.resetUserStreak(userEmail);
        return ResponseEntity.noContent().build();
    }
}
