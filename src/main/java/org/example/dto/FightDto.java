package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FightDto {
    private Long id;
    private String fighter1;
    private String fighter2;
    private String result;
    private String weightClass;
    private LocalDate date;
}
