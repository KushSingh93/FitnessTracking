package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dao.FavoriteExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.dto.ExerciseResponseDTO;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExercisesService {

    private final ExercisesDao exercisesDao;
    private final UsersDao usersDao;
    private final FavoriteExercisesDao favoriteExercisesDao;

    @Autowired
    public ExercisesService(ExercisesDao exercisesDao, UsersDao usersDao, FavoriteExercisesDao favoriteExercisesDao) {
        this.exercisesDao = exercisesDao;
        this.usersDao = usersDao;
        this.favoriteExercisesDao = favoriteExercisesDao;
    }

    // ✅ Get all exercises for a user (includes their custom + admin exercises)
    public List<Exercises> getAllExercisesForUser(String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Long> adminIds = usersDao.getAllAdmins().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        return exercisesDao.getAllExercisesForUser(user.getUserId(), adminIds);
    }

    public List<ExerciseResponseDTO> getAllExercisesForUserWithFavourite(String userEmail) {
        List<ExerciseResponseDTO> getAllExercisesList = new ArrayList<>();
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Long> adminIds = usersDao.getAllAdmins().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        List<Exercises> exercises = exercisesDao.getAllExercisesForUser(user.getUserId(), adminIds);
        for (Exercises exercise : exercises) {
            ExerciseResponseDTO getAllExercises = new ExerciseResponseDTO();
            getAllExercises.setExerciseId(exercise.getExerciseId());
            getAllExercises.setExerciseName(exercise.getExerciseName());
            getAllExercises.setBodyPart(exercise.getBodyPart());
            getAllExercises.setCaloriesBurntPerRep(exercise.getCaloriesBurntPerRep());
            FavoriteExercises favoriteExercises = favoriteExercisesDao.getFavoritesByUserIdAndExcerciseId(user.getUserId(), exercise.getExerciseId());
            if (favoriteExercises != null) {
                getAllExercises.setFavourite(Boolean.FALSE.equals(favoriteExercises.getDeleted()));
            } else{
                getAllExercises.setFavourite(false);
            }
            getAllExercisesList.add(getAllExercises);
        }
        return getAllExercisesList;
    }

    // ✅ Get exercise by ID
    public Exercises getExerciseById(Long exerciseId) {
        return exercisesDao.getExerciseById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));
    }

    // ✅ Get exercises by body part using enum
    public List<Exercises> getExercisesByBodyPart(String bodyPart, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Long> adminIds = usersDao.getAllAdmins().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        try {
            BodyPart bodyPartEnum = BodyPart.valueOf(bodyPart.toUpperCase());
            return exercisesDao.getExercisesByBodyPart(bodyPartEnum, user.getUserId(), adminIds);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid body part: " + bodyPart);
        }
    }

    // ✅ Save a new custom exercise
    @Transactional
    public Exercises addExercise(ExerciseRequest exerciseRequest, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Exercises exercise = new Exercises();
        exercise.setUser(user);
        exercise.setExerciseName(exerciseRequest.getExerciseName());

        if (exerciseRequest.getCaloriesBurntPerRep() == null) {
            throw new RuntimeException("Calories burnt per set cannot be null.");
        }
        exercise.setCaloriesBurntPerRep(exerciseRequest.getCaloriesBurntPerRep());

        if (exerciseRequest.getBodyPart() == null) {
            throw new RuntimeException("Body part cannot be null.");
        }

        try {
            exercise.setBodyPart(exerciseRequest.getBodyPart());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid body part: " + exerciseRequest.getBodyPart() +
                    ". Allowed values: CHEST, BACK, ARMS, LEGS, SHOULDER, ABS");
        }

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
