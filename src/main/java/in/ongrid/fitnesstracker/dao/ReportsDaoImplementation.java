package in.ongrid.fitnesstracker.dao;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class ReportsDaoImplementation implements ReportsDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<WorkoutExercises> getWorkoutSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        System.out.println("Fetching workout summary for User ID: " + userId);
        System.out.println("Start Date: " + startDate + " | End Date: " + endDate);

        TypedQuery<WorkoutExercises> query = entityManager.createQuery(
                "SELECT we FROM WorkoutExercises we " +
                        "JOIN we.workout w " +
                        "WHERE w.user.userId = :userId " +
                        "AND w.date BETWEEN :startDate AND :endDate",
                WorkoutExercises.class);

        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<WorkoutExercises> results = query.getResultList();
        System.out.println("Workout Exercises Found: " + results.size());

        for (WorkoutExercises we : results) {
            System.out.println("Workout Exercise ID: " + we.getWorkoutExerciseId() +
                    " | Exercise: " + we.getExercise().getExerciseName() +
                    " | Sets: " + we.getSets() +
                    " | Reps: " + we.getReps() +
                    " | Body Part: " + we.getExercise().getBodyPart());
        }

        return results;
    }
}
