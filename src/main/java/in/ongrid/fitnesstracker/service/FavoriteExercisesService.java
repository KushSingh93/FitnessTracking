package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.FavoriteExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dto.FavoriteRequest;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteExercisesService {

    private final FavoriteExercisesDao favoriteExercisesDao;
    private final UsersDao usersDao;
    private final ExercisesDao exercisesDao;

    public FavoriteExercisesService(FavoriteExercisesDao favoriteExercisesDao, UsersDao usersDao, ExercisesDao exercisesDao) {
        this.favoriteExercisesDao = favoriteExercisesDao;
        this.usersDao = usersDao;
        this.exercisesDao = exercisesDao;
    }

    // ✅ Retrieve all favorite exercises for a user
    public List<FavoriteExercises> getFavoritesByUserId(Long userId) {
        return favoriteExercisesDao.getFavoritesByUserId(userId);
    }

    // ✅ Add an exercise to favorites
    public FavoriteExercises addFavoriteExercise(FavoriteRequest favoriteRequest, String userEmail) {
        // Fetch user by email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Fetch exercise by ID
        Exercises exercise = exercisesDao.getExerciseById(favoriteRequest.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));

        // Create and save favorite exercise entry
        FavoriteExercises favoriteExercise = new FavoriteExercises();
        favoriteExercise.setUser(user);
        favoriteExercise.setExercise(exercise);

        return favoriteExercisesDao.saveFavoriteExercise(favoriteExercise);
    }

    // ✅ Remove an exercise from favorites
    public void deleteFavoriteExercise(Long exerciseId, String userEmail) {
        // Fetch the user by email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if the exercise exists
        Exercises exercise = exercisesDao.getExerciseById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));

        // Check if the exercise is actually in the user's favorites
        Optional<FavoriteExercises> favoriteExercise = favoriteExercisesDao.findByUserAndExercise(user, exercise);
        if (favoriteExercise.isEmpty()) {
            throw new RuntimeException("Exercise is not in your favorites!");
        }

        // Delete the favorite
        favoriteExercisesDao.deleteFavoriteExercise(favoriteExercise.get().getFavoriteId());
    }
}
