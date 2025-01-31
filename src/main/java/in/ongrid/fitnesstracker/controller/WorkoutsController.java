//package in.ongrid.fitnesstracker.controller;
//
//import in.ongrid.fitnesstracker.model.entities.Workouts;
//import in.ongrid.fitnesstracker.service.WorkoutsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/workouts")
//public class WorkoutsController {
//
//    @Autowired
//    private WorkoutsService workoutsService;
//
//    // ✅ Get all workouts for a user
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Workouts>> getUserWorkouts(@PathVariable Long userId) {
//        return ResponseEntity.ok(workoutsService.getWorkoutsByUserId(userId));
//    }
//
//    // ✅ Create a new workout
//    @PostMapping
//    public ResponseEntity<Workouts> createWorkout(@RequestBody Workouts workout) {
//        return ResponseEntity.ok(workoutsService.createWorkout(workout));
//    }
//
//    // ✅ Delete a workout
//    @DeleteMapping("/{workoutId}")
//    public ResponseEntity<Void> deleteWorkout(@PathVariable Long workoutId) {
//        workoutsService.deleteWorkout(workoutId);
//        return ResponseEntity.noContent().build();
//    }
//}
