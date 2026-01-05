package com.twitterclone.api.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "E-posta boY olamaz")
    @Email(message = "GeÇõerli bir e-posta adresi girin")
    private String email;

    @NotBlank(message = "?ifre boY olamaz")
    private String password;
}
