package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.dao.ExercisesDaoImplementation;


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
    @GetMapping("/allExercises")
    public ResponseEntity<List<Exercises>> getAllExercises() {
        return ResponseEntity.ok(exercisesService.getAllExercises());
    }

    // ✅ Get exercise by ID
    @GetMapping("/{exerciseId}")
    public ResponseEntity<Exercises> getExerciseById(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(exercisesService.getExerciseById(exerciseId));
    }

    // ✅ Get exercises by body part
    @GetMapping("/bodyPart/{bodyPart}")
    public ResponseEntity<List<Exercises>> getExercisesByBodyPart(@PathVariable String bodyPart) {
        return ResponseEntity.ok(exercisesService.getExercisesByBodyPart(bodyPart));
    }

    // ✅ Add a custom exercise
    @RequestMapping("/exercises")
    public class ExerciseController {

        private final ExercisesService exercisesService;

        public ExerciseController(ExercisesService exercisesService) {
            this.exercisesService = exercisesService;
        }

        @PostMapping("/addExercise")
        public ResponseEntity<Exercises> addExercise(
                @Valid @RequestBody ExerciseRequest exerciseRequest, // Validate input fields
                @RequestHeader("Authorization") String token) { // Get token from header

            // Extract actual JWT token (remove "Bearer " prefix)
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String userEmail = jwtUtil.extractEmail(jwtToken); // Extract email from token

            Exercises savedExercise = exercisesService.createExercise(exerciseRequest, userEmail);
            return ResponseEntity.ok(savedExercise);
        }
    }

    // ✅ Delete a custom exercise (only if created by the user)
    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable Long exerciseId,
            @RequestParam String userEmail) {
        exercisesService.deleteExercise(exerciseId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
