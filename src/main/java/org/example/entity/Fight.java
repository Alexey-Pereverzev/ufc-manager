package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "fight")
@Builder
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fight_seq")
    @SequenceGenerator(name = "fight_seq", sequenceName = "seq_fight", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "fight")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RatingEntry> ratingEntries;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "result")
    private String result;

    @Column(name = "unique_description")
    private String uniqueDescription;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "sequence_in_total")
    private int sequenceInTotal;

    @ManyToOne
    @JoinColumn(name = "fighter1_id", nullable = false)
    private Fighter fighter1;

    @ManyToOne
    @JoinColumn(name = "fighter2_id", nullable = false)
    private Fighter fighter2;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Fighter winner;

    @ManyToOne
    @JoinColumn(name = "weight_class_id", nullable = false)
    private WeightClass weightClass;
}
