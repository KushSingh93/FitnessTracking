package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.User;  // ✅ Correct import
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
    public void setDeletedById(Long id, Long userId) {
        Query query = entityManager.createQuery("Update FavoriteExercises f set f.deleted = :deleted where f.user.userId = :userId and f.exercise.exerciseId = :exerciseId");
        query.setParameter("deleted", true);
        query.setParameter("exerciseId", id);
        query.setParameter("userId", userId);
        query.executeUpdate();
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

    @Override
    public FavoriteExercises getFavoritesByUserIdAndExcerciseId(Long userId, Long exerciseId) {
        try{
            return entityManager.createQuery(
                            "SELECT f FROM FavoriteExercises f WHERE f.user.userId = :userId and f.exercise.exerciseId = :exerciseId and f.deleted = false", FavoriteExercises.class)
                    .setParameter("userId", userId)
                    .setParameter("exerciseId", exerciseId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
