package in.ongrid.fitnesstracker.controller;

import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
public class ExercisesController {

    @Autowired
    private ExercisesService exercisesService;

    // ✅ Get all exercises (Predefined & Custom)
    @GetMapping
    public ResponseEntity<List<Exercises>> getAllExercises() {
        return ResponseEntity.ok(exercisesService.getAllExercises());
    }

    // ✅ Add a custom exercise (Admins only for predefined exercises)
    @PostMapping
    public ResponseEntity<Exercises> addExercise(@RequestBody Exercises exercise) {
        return ResponseEntity.ok(exercisesService.createExercise(exercise));
    }

    // ✅ Delete a custom exercise (only if created by the user)
    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long exerciseId) {
        exercisesService.deleteExercise(exerciseId);
        return ResponseEntity.noContent().build();
    }
}
