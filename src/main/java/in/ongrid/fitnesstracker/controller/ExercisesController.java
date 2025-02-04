package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.service.ExercisesService;
import in.ongrid.fitnesstracker.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExercisesController {

    private final ExercisesService exercisesService;
    private final JwtUtil jwtUtil;

    public ExercisesController(ExercisesService exercisesService, JwtUtil jwtUtil) {
        this.exercisesService = exercisesService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Get all exercises (Predefined & Custom)
    @GetMapping("/getAllExercises")
    public ResponseEntity<List<Exercises>> getAllExercises(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        return ResponseEntity.ok(exercisesService.getAllExercisesForUser(userEmail));
    }

    // ✅ Get exercise by ID
    @GetMapping("/{exerciseId}")
    public ResponseEntity<Exercises> getExerciseById(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(exercisesService.getExerciseById(exerciseId));
    }

    // ✅ Get exercises by body part (Now filtered by user + admin)
    @GetMapping("/bodyPart/{bodyPart}")
    public ResponseEntity<List<Exercises>> getExercisesByBodyPart(
            @PathVariable String bodyPart,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        return ResponseEntity.ok(exercisesService.getExercisesByBodyPart(bodyPart, userEmail));
    }

    // ✅ Add a custom exercise
    @PostMapping("/addExercise")
    public ResponseEntity<Exercises> addExercise(
            @Valid @RequestBody ExerciseRequest exerciseRequest, // Validate input fields
            @RequestHeader("Authorization") String token) { // Get token from header

        // Extract actual JWT token (remove "Bearer " prefix)
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        Exercises savedExercise = exercisesService.addExercise(exerciseRequest, userEmail);
        return ResponseEntity.ok(savedExercise);
    }

    // ✅ Delete a custom exercise (only if created by the user)
    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable Long exerciseId,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

        exercisesService.deleteExercise(exerciseId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
