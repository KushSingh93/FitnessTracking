package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.WorkoutsDao;
import in.ongrid.fitnesstracker.dto.WorkoutRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import in.ongrid.fitnesstracker.service.StreaksService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutsService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutsService.class);

    private final WorkoutsDao workoutsDao;
    private final UsersDao usersDao;
    private final StreaksService streaksService;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkoutsService(WorkoutsDao workoutsDao, UsersDao usersDao, StreaksService streaksService) {
        this.workoutsDao = workoutsDao;
        this.usersDao = usersDao;
        this.streaksService = streaksService;
    }

    //  Get all workouts for a user by email
    public List<Workouts> getWorkoutsByUserEmail(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return workoutsDao.getWorkoutsByUser(user);
    }

    //  Create a new workout (calls streak service after saving)
    @Transactional
    public Workouts createWorkout(WorkoutRequest workoutRequest, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        LocalDate workoutDate = (workoutRequest.getDate() != null) ? workoutRequest.getDate() : LocalDate.now();
        logger.info(" Workout Date: {}", workoutDate);

        Workouts workout = new Workouts();
        workout.setUser(user);
        workout.setDate(workoutDate);

        Workouts savedWorkout = workoutsDao.saveWorkout(workout);
        entityManager.flush(); // Ensures workout is saved before streak updates

        logger.info(" Workout created for {} on {}", userEmail, savedWorkout.getDate());

        // Call StreaksService to update streaks (instead of handling it here)
        streaksService.getUserStreak(userEmail);

        return savedWorkout;
    }

    // Delete a workout
    @Transactional
    public void deleteWorkout(Long workoutId, String userEmail) {
        Workouts workout = workoutsDao.getWorkoutById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found!"));

        if (!workout.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own workouts!");
        }

        workoutsDao.deleteWorkout(workoutId);
        logger.info(" Workout deleted for {} on {}", userEmail, workout.getDate());

        //Recalculate streak after deletion
        streaksService.getUserStreak(userEmail);
    }
}
