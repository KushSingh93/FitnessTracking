package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dto.WorkoutRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutsService {

    private final WorkoutsDao workoutsDao;
    private final UsersDao usersDao;

    public WorkoutsService(WorkoutsDao workoutsDao, UsersDao usersDao) {
        this.workoutsDao = workoutsDao;
        this.usersDao = usersDao;
    }

    // ✅ Get all workouts for a user
    public List<Workouts> getWorkoutsByUserEmail(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return workoutsDao.getWorkoutsByUser(user);
    }

    // ✅ Create a new workout
    public Workouts createWorkout(WorkoutRequest workoutRequest, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Workouts workout = new Workouts();
        workout.setUser(user);
        workout.setDate(workoutRequest.getDate());

        return workoutsDao.saveWorkout(workout);
    }

    // ✅ Delete a workout
    public void deleteWorkout(Long workoutId, String userEmail) {
        Workouts workout = workoutsDao.getWorkoutById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found!"));

        if (!workout.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own workouts!");
        }

        workoutsDao.deleteWorkout(workoutId);
    }
}
