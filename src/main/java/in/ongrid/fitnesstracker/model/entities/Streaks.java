package in.ongrid.fitnesstracker.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "streaks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Streaks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streakId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private Integer streakCount = 0;
}
