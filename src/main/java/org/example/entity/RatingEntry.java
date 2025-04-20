package org.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@Table(name = "rating_entry")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_entry_seq")
    @SequenceGenerator(name = "rating_entry_seq", sequenceName = "seq_rating_entry", allocationSize = 1)
    private Long id;

    @Column(name = "ranking")
    private double ranking;

    @ManyToOne
    @JoinColumn(name = "fight_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Fight fight;

    @ManyToOne
    @JoinColumn(name = "fighter_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Fighter fighter;

    @ManyToOne
    @JoinColumn(name = "weight_class_id")
    private WeightClass weightClass;
}
