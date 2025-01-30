package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class FavoriteExercisesDaoImplementation implements FavoriteExercisesDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FavoriteExercises> getFavoritesByUserId(Long userId) {
        return entityManager.createQuery("SELECT fe FROM FavoriteExercises fe WHERE fe.user.userId = :userId", FavoriteExercises.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public FavoriteExercises saveFavoriteExercise(FavoriteExercises favoriteExercise) {
        entityManager.persist(favoriteExercise);
        return favoriteExercise;
    }

    @Override
    public void deleteFavoriteExercise(Long favoriteId) {
        FavoriteExercises favoriteExercise = entityManager.find(FavoriteExercises.class, favoriteId);
        if (favoriteExercise != null) {
            entityManager.remove(favoriteExercise);
        }
    }
}
