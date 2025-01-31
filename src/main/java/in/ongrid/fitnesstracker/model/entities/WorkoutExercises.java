package in.ongrid.fitnesstracker.model.entities;
import in.ongrid.fitnesstracker.model.entities.Exercises;
import in.ongrid.fitnesstracker.model.entities.Workouts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workout_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutExerciseId;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false, referencedColumnName = "workoutId")
    private Workouts workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false, referencedColumnName = "exerciseId")
    private Exercises exercise;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;
}
