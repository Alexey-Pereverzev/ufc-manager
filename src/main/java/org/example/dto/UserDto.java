package org.example.dto;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private String status;
}



