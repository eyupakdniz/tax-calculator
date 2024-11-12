package com.eyup.challenge.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
}

