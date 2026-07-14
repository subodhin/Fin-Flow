package com.finflow.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String firstName;


    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    private String password;
}