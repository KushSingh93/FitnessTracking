package in.ongrid.fitnesstracker.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "favorite_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "exerciseId", nullable = false)
    private Exercises exercise;

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @Builder.Default
    private Boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
    }
}