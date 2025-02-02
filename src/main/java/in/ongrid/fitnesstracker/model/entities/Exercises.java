package in.ongrid.fitnesstracker.model.entities;

import in.ongrid.fitnesstracker.model.entities.WorkoutExercises;
import in.ongrid.fitnesstracker.model.enums.BodyPart;
import in.ongrid.fitnesstracker.model.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class Exercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    @Column(nullable = false, unique = true, length = 100)
    private String exerciseName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private BodyPart bodyPart;

    @Column(nullable = false)
    private Float caloriesBurntPerSet;  // ✅ Added based on schema

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)  // Match the column name
    private User user;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercises> workoutExercises;

}

