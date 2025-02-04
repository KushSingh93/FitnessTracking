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

    // ✅ Get & Auto-Update User Streak
    @GetMapping("/getStreak")
    public ResponseEntity<StreaksRequest> getUserStreak(@RequestHeader("Authorization") String token) {
        String userEmail = extractUserEmail(token);
        return ResponseEntity.ok(streaksService.getAndUpdateUserStreak(userEmail));
    }

    // ✅ Utility Method to Extract User Email from JWT Token
    private String extractUserEmail(String token) {
        return jwtUtil.extractEmail(token.startsWith("Bearer ") ? token.substring(7) : token);
    }
}
