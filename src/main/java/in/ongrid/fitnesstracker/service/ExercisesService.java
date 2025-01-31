package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExercisesService {

    private final ExercisesDao exercisesDao;
    private final UsersDao usersDao;

    public ExercisesService(ExercisesDao exercisesDao, UsersDao usersDao) {
        this.exercisesDao = exercisesDao;
        this.usersDao = usersDao;
    }

    // ✅ Get all exercises
    public List<Exercises> getAllExercises() {
        return exercisesDao.getAllExercises();
    }

    // ✅ Get exercise by ID
    public Exercises getExerciseById(Long exerciseId) {
        return exercisesDao.getExerciseById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    // ✅ Get exercises by body part
    public List<Exercises> getExercisesByBodyPart(String bodyPart) {
        return exercisesDao.getExercisesByBodyPart(bodyPart);
    }

//     ✅ Create a custom exercise
//    public Exercises createExercise(ExerciseRequest exerciseRequest, String userEmail) {
//        // Fetch user by email
//        User user = usersDao.getUserByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found!"));
//
//        // Convert DTO to Entity
//        Exercises exercise = new Exercises();
//        exercise.setUser(user); // Correct mapping
//        exercise.setName(exerciseRequest.getExerciseName());
//        exercise.set(exerciseRequest.getReps());
//        exercise.setSets(exerciseRequest.getSets());
//
//        // Save and return the created exercise
//        return exercisesDao.save(exercise);
//    }

    // ✅ Delete an exercise (Only if created by the user)
//    public void deleteExercise(Long exerciseId, String userEmail) {
//        Exercises exercise = getExerciseById(exerciseId);
//
//        if (!exercise.getId().getEmail().equals(userEmail)) {
//            throw new RuntimeException("You can only delete your own exercises!");
//        }
//
//        exercisesDao.deleteExercise(exerciseId);
//    }
}
