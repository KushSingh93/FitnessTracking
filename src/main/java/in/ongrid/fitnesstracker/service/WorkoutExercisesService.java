package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.WorkoutExercisesDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.WorkoutExercisesRequest;
import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutExercisesService {

    private final WorkoutsDao workoutsDao;
    private final ExercisesDao exercisesDao;
    private final WorkoutExercisesDao workoutExercisesDao;
    private final UsersDao usersDao;

    public WorkoutExercisesService(WorkoutsDao workoutsDao, ExercisesDao exercisesDao, WorkoutExercisesDao workoutExercisesDao, UsersDao usersDao) {
        this.workoutsDao = workoutsDao;
        this.exercisesDao = exercisesDao;
        this.workoutExercisesDao = workoutExercisesDao;
        this.usersDao = usersDao;
    }

    // ✅ Add an exercise to a workout (With user validation)
    public WorkoutExercisesRequest addExerciseToWorkout(WorkoutExercisesRequest request, String userEmail) {
        // Get user from email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Fetch workout and verify ownership
//        Workouts workout = workoutsDao.getWorkoutById(workoutId)
//                .orElseThrow(() -> new RuntimeException("Workout not found!"));

        Workouts workout = workoutExercisesDao.getWorkoutByUserAndDate(user.getUserId(), LocalDate.now());

        if(workout == null) {
            workout = new Workouts();
            workout.setUser(user);
            workout.setDate(LocalDate.now());
            workout = workoutsDao.saveWorkout(workout);
        }

        if (!workout.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You can only add exercises to your own workouts!");
        }

        // Fetch exercise
        Exercises exercise = exercisesDao.getExerciseById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));

        // Create workout exercise entry
        WorkoutExercises workoutExercise = new WorkoutExercises();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(request.getSets());
        workoutExercise.setReps(request.getReps());

        WorkoutExercises savedWorkoutExercise = workoutExercisesDao.saveWorkoutExercise(workoutExercise);

        return new WorkoutExercisesRequest(
                savedWorkoutExercise.getWorkoutExerciseId(),
                workout.getWorkoutId(),
                exercise.getExerciseId(),
                exercise.getExerciseName(),
                savedWorkoutExercise.getSets(),
                savedWorkoutExercise.getReps(),
                exercise.getCaloriesBurntPerRep()
        );
    }


    // ✅ Get all exercises in a workout (With user validation)
    public List<WorkoutExercisesRequest> getWorkoutExercises(Long workoutId, String userEmail) {
        // Fetch the workout and validate ownership
        Workouts workout = workoutsDao.getWorkoutById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found!"));

        // Get user from email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if the user owns the workout
        if (!workout.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You can only view exercises in your own workouts!");
        }

        return workoutExercisesDao.getWorkoutExercisesByWorkout(workoutId)
                .stream()
                .map(we -> new WorkoutExercisesRequest(
                        we.getWorkoutExerciseId(),
                        we.getWorkout().getWorkoutId(),
                        we.getExercise().getExerciseId(),
                        we.getExercise().getExerciseName(),
                        we.getSets(),
                        we.getReps(),
                        we.getExercise().getCaloriesBurntPerRep()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get all exercises in a workout by date (With user validation)
    public List<WorkoutExercisesRequest> getExercisesByDate(LocalDate date, String userEmail) {
        // Get user from email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Fetch all exercises linked to the workout on this date
        return workoutExercisesDao.getExercisesByUserAndDate(user.getUserId(), date)
                .stream()
                .map(we -> new WorkoutExercisesRequest(
                        we.getWorkoutExerciseId(),
                        we.getWorkout().getWorkoutId(),
                        we.getExercise().getExerciseId(),
                        we.getExercise().getExerciseName(),
                        we.getSets(),
                        we.getReps(),
                        we.getExercise().getCaloriesBurntPerRep()
                ))
                .collect(Collectors.toList());
    }



    // ✅ Remove an exercise from a workout (With user validation)
    public void removeExerciseFromWorkout(Long workoutExerciseId, String userEmail) {
        Optional<WorkoutExercises>  workoutExercises = Optional.ofNullable(workoutExercisesDao.getWorkoutExerciseById(workoutExerciseId)
                .orElseThrow(() -> new RuntimeException("Workout exercise not found!")));

        // Verify ownership
        WorkoutExercises workoutExercise = workoutExercises.get();
        if (!workoutExercise.getWorkout().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only remove exercises from your own workouts!");
        }

        workoutExercisesDao.deleteWorkoutExercise(workoutExerciseId);
    }
}
