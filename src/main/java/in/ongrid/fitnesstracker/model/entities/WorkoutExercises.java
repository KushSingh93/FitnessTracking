package in.ongrid.fitnesstracker.model.entities;

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
    @JoinColumn(name = "workout_id", referencedColumnName = "workoutId", nullable = false)
    private Workouts workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "exerciseId", nullable = false)
    private Exercises exercise;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false; // ✅ Renamed from "isDeleted" to "deleted"

    // ✅ Soft delete method
    public void softDelete() {
        this.deleted = true;
    }
}
