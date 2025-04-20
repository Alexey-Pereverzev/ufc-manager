package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Country {
    @Id
    @Column(length = 2)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "flag_url")
    private String flagUrl;
}
