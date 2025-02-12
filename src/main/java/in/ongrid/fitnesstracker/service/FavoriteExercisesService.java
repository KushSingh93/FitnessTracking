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

    public FavoriteExercises getFavoriteByUserIdAndExcercise(Long userId, Long exerciseId) {
        return favoriteExercisesDao.getFavoritesByUserIdAndExcerciseId(userId, exerciseId);
    }

    // ✅ Add an exercise to favorites
    public FavoriteExercises addFavoriteExercise(FavoriteRequest favoriteRequest, String userEmail) {
        // Fetch user by email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Fetch exercise by ID
        Exercises exercise = exercisesDao.getExerciseById(favoriteRequest.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));
        FavoriteExercises existingFavoriteExercise = favoriteExercisesDao.getFavoritesByUserIdAndExcerciseId(user.getUserId(), exercise.getExerciseId());
        if (existingFavoriteExercise != null) {
            existingFavoriteExercise.setDeleted(false);
            return favoriteExercisesDao.saveFavoriteExercise(existingFavoriteExercise);
        } else {
            // Create and save favorite exercise entry
            FavoriteExercises favoriteExercise = new FavoriteExercises();
            favoriteExercise.setUser(user);
            favoriteExercise.setExercise(exercise);
            return favoriteExercisesDao.saveFavoriteExercise(favoriteExercise);

        }
    }

    // Remove an exercise from favorites (soft delete)
    public void deleteFavoriteExercise(Long favExerciseId, String userEmail) {
        // Fetch the user by email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));
//        boolean isValid = favoriteExercisesDao.isUserValid(user, favExerciseId);
        // Soft delete by setting the 'deleted' flag to true
//        if (isValid) {
            favoriteExercisesDao.setDeletedById(favExerciseId, user.getUserId());
//        }
//        else{
//            throw new RuntimeException("User is not Authorized");
//        }
    }




}
