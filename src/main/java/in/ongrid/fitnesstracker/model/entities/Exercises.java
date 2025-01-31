package in.ongrid.fitnesstracker.model.entities;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Exercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exerciseName;
    private int reps;
    private int sets;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercises> workoutExercises = new ArrayList<>();
}
