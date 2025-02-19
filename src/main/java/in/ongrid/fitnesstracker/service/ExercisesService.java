package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.ExercisesDao;
import in.ongrid.fitnesstracker.dao.ExercisesDaoImplementation;
import in.ongrid.fitnesstracker.dao.FavoriteExercisesDao;
import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.ExerciseRequest;
import in.ongrid.fitnesstracker.dto.ExerciseResponseDTO;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.FavoriteExercises;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import in.ongrid.fitnesstracker.model.enums.UserType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExercisesService {

    private final ExercisesDao exercisesDao;
    private final UsersDao usersDao;
    private final FavoriteExercisesDao favoriteExercisesDao;
    private final ExercisesDaoImplementation exercisesDaoImplementation;

    @Autowired
    public ExercisesService(ExercisesDao exercisesDao, UsersDao usersDao, FavoriteExercisesDao favoriteExercisesDao, ExercisesDaoImplementation exercisesDaoImplementation) {
        this.exercisesDao = exercisesDao;
        this.usersDao = usersDao;
        this.favoriteExercisesDao = favoriteExercisesDao;
        this.exercisesDaoImplementation = exercisesDaoImplementation;
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

            // Set the userId to the actual user ID who created the exercise
            getAllExercises.setUserId(exercise.getUser().getUserId());

            FavoriteExercises favoriteExercises = favoriteExercisesDao.getFavoritesByUserIdAndExcerciseId(user.getUserId(), exercise.getExerciseId());
            if (favoriteExercises != null) {
                getAllExercises.setFavourite(Boolean.FALSE.equals(favoriteExercises.getDeleted()));
            } else {
                getAllExercises.setFavourite(false);
            }
            getAllExercisesList.add(getAllExercises);
        }
        return getAllExercisesList;
    }



    //  Get exercise by ID
    public Exercises getExerciseById(Long exerciseId) {
        return exercisesDao.getExerciseById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));
    }

    //  Get exercises by body part using enum
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

    //  Save a new custom exercise
    @Transactional
    public Exercises addExercise(ExerciseRequest exerciseRequest, String userEmail) {
       try{
           User user = usersDao.getUserByEmail(userEmail)
                   .orElseThrow(() -> new RuntimeException("User not found!"));
           Exercises exercisedupcheck = exercisesDaoImplementation.getExerciseByName(exerciseRequest , user.getUserId());

           if(exercisedupcheck != null){
               System.out.println("Exercise already exists!");
               throw new RuntimeException("Exercise already exists!");
           }

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
       }catch (Exception e) {
           log.warn(e.getMessage());
       }
       return null;
    }

    // Soft delete custom exercise:
    @Transactional
    public void softDeleteExercise(Long exerciseId, String userEmail) {
        User user = usersDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Exercises exercise = exercisesDao.getExerciseById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found!"));

        // Check if the exercise belongs to the user and is not an admin exercise
        if (!exercise.getUser().getUserId().equals(user.getUserId()) || exercise.getUser().getUserType() == UserType.ADMIN) {
            throw new RuntimeException("You don't have permission to delete this exercise.");
        }

        exercisesDao.softDeleteExercise(exerciseId);
    }


}
