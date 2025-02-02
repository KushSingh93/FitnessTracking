package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.WorkoutRequest;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import in.ongrid.fitnesstracker.service.WorkoutsService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutsController {

    private final WorkoutsService workoutsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public WorkoutsController(WorkoutsService workoutsService, JwtUtil jwtUtil) {
        this.workoutsService = workoutsService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Get all workouts for a user
    @GetMapping("/userWorkout")
    public ResponseEntity<List<Workouts>> getUserWorkouts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);
        return ResponseEntity.ok(workoutsService.getWorkoutsByUserEmail(userEmail));
    }

    // ✅ Create a new workout
    @PostMapping("/createWorkout")
    public ResponseEntity<Workouts> createWorkout(
            @Valid @RequestBody WorkoutRequest workoutRequest,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        return ResponseEntity.ok(workoutsService.createWorkout(workoutRequest, userEmail));
    }

    // ✅ Delete a workout
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long workoutId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        workoutsService.deleteWorkout(workoutId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
