package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FighterDto {
    private String name;
    private String country;
    private String weightClass;
    private String ufcRecord;
    private String mmaRecord;
    private String status; // "Active" or "Released"
}