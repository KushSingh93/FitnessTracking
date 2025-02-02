package in.ongrid.fitnesstracker.dao;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;

import java.util.List;
import java.util.Optional;

public interface FavoriteExercisesDao {
    List<FavoriteExercises> getFavoritesByUserId(Long userId);
    FavoriteExercises saveFavoriteExercise(FavoriteExercises favoriteExercise);
    void deleteFavoriteExercise(Long favoriteId);
    Optional<FavoriteExercises> findByUserAndExercise(User user, Exercises exercise);
}
