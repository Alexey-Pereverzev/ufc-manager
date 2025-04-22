package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "fighter")
@Builder
public class Fighter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "fighter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RatingEntry> ratingEntries;

    @Column(name = "min_weight_class")
    private int minWeightClassNumber;

    @Column(name = "max_weight_class")
    private int maxWeightClassNumber;

    @Column(name = "ufc_record")
    private String ufcRecord;

    @Column(name = "mma_record")
    private String mmaRecord;

    @Column(name = "elo_rating")
    private double eloRating;

    @Column(name = "is_active")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "country_code")
    private Country country;
}
