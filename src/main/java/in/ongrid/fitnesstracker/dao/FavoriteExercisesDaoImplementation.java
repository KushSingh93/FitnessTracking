package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;  // ✅ Correct import
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class FavoriteExercisesDaoImplementation implements FavoriteExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ Retrieve all favorite exercises of a user
    @Override
    public List<FavoriteExercises> getFavoritesByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT f FROM FavoriteExercises f WHERE f.user.userId = :userId", FavoriteExercises.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // ✅ Save a new favorite exercise
    @Override
    public FavoriteExercises saveFavoriteExercise(FavoriteExercises favoriteExercise) {
        entityManager.persist(favoriteExercise);
        return favoriteExercise;
    }

    // ✅ Delete a favorite exercise by ID

    @Override
    public void deleteFavoriteExercise(Long favoriteId) {
        FavoriteExercises favoriteExercise = entityManager.find(FavoriteExercises.class, favoriteId);
        if (favoriteExercise != null) {
            entityManager.remove(favoriteExercise);
        }
    }

    @Override
    public Optional<FavoriteExercises> findByUserAndExercise(User user, Exercises exercise) {
        List<FavoriteExercises> results = entityManager.createQuery(
                        "SELECT f FROM FavoriteExercises f WHERE f.user = :user AND f.exercise = :exercise", FavoriteExercises.class)
                .setParameter("user", user)
                .setParameter("exercise", exercise)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
