package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "ratingEntries")
@Table(name = "weight_class")
public class WeightClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @OneToMany(mappedBy = "weightClass")
    private List<RatingEntry> ratingEntries;
}
