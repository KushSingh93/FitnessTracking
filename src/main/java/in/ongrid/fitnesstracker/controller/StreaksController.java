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

    @GetMapping("/getStreak")
    public ResponseEntity<StreaksRequest> getUserStreak(@RequestHeader("Authorization") String token) {
        String userEmail = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok(streaksService.getUserStreak(userEmail));
    }
}
