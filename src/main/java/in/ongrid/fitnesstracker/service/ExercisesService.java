package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));
    }

    // ✅ Get exercises by body part using enum
    public List<Exercises> getExercisesByBodyPart(String bodyPart) {
        try {
            BodyPart bodyPartEnum = BodyPart.valueOf(bodyPart.toUpperCase()); // Convert to enum
            return exercisesDao.getExercisesByBodyPart(bodyPartEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid body part: " + bodyPart);
        }
    }

    // ✅ Save a new exercise
    @Transactional
    public Exercises addExercise(ExerciseRequest exerciseRequest, String userEmail) {
        // Fetch user by email
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Convert DTO to Entity
        Exercises exercise = new Exercises();
        exercise.setUser(user);
        exercise.setExerciseName(exerciseRequest.getExerciseName());
        if (exerciseRequest.getCaloriesBurntPerSet() == null) {
            throw new RuntimeException("Calories burnt per set cannot be null.");
        }

        exercise.setCaloriesBurntPerSet(exerciseRequest.getCaloriesBurntPerSet());

        // Validate and Convert BodyPart from Request
        if (exerciseRequest.getBodyPart() == null) {
            throw new RuntimeException("Body part cannot be null.");
        }

        try {
            // Directly assign the enum value
            exercise.setBodyPart(exerciseRequest.getBodyPart());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid body part: " + exerciseRequest.getBodyPart() +
                    ". Allowed values: CHEST, BACK, ARMS, LEGS, SHOULDER, ABS");
        }

        // Save and return the created exercise
        return exercisesDao.saveExercise(exercise);
    }


    // ✅ Delete an exercise (Only if created by the user)
    public void deleteExercise(Long exerciseId, String userEmail) {
        Exercises exercise = getExerciseById(exerciseId);

        if (!exercise.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own exercises!");
        }

        exercisesDao.deleteExercise(exerciseId);
    }
}
