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
    @JoinColumn(name = "userId")
    private Users userId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private Integer streakCount = 0;
}
