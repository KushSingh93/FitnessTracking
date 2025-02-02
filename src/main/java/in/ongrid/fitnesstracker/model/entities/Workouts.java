package in.ongrid.fitnesstracker.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workouts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now(); // ✅ Set creation date
        }
        if (this.date == null) {
            this.date = LocalDate.now(); // ✅ Ensure the workout date is set
        }
    }
}
