package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.WorkoutExercisesRequest;
import in.ongrid.fitnesstracker.dto.WorkoutResponse;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.service.WorkoutExercisesService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout-exercises")
public class WorkoutExercisesController {

    private final WorkoutExercisesService workoutExercisesService;
    private final JwtUtil jwtUtil;

    public WorkoutExercisesController(WorkoutExercisesService workoutExercisesService, JwtUtil jwtUtil) {
        this.workoutExercisesService = workoutExercisesService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Add an exercise to a workout (🔒 Requires Authentication)
    @PostMapping("/add")
    public ResponseEntity<WorkoutExercisesRequest> addExerciseToWorkout(
            @RequestBody WorkoutExercisesRequest request,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        return ResponseEntity.ok(workoutExercisesService.addExerciseToWorkout(request, userEmail));
    }


    // ✅ Get all exercises in a workout (🔒 Requires Authentication, Only Owner Can View)
    @GetMapping("/{workoutId}")
    public ResponseEntity<List<WorkoutExercisesRequest>> getWorkoutExercises(
            @PathVariable Long workoutId,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        return ResponseEntity.ok(workoutExercisesService.getWorkoutExercises(workoutId, userEmail));
    }

    //  Remove an exercise from a workout
    @DeleteMapping("/remove/{workoutExerciseId}")
    public ResponseEntity<Void> removeExerciseFromWorkout(
            @PathVariable Long workoutExerciseId,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        workoutExercisesService.removeExerciseFromWorkout(workoutExerciseId, userEmail);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("byDate/{date}")
    public ResponseEntity<List<WorkoutExercisesRequest>> getExercisesByDate(
            @PathVariable LocalDate date,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken);

        List<WorkoutExercisesRequest> exercises = workoutExercisesService.getExercisesByDate(date, userEmail);
        return ResponseEntity.ok(exercises);
    }
}
