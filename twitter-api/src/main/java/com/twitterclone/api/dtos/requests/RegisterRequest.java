package com.twitterclone.api.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "KullanŽñcŽñ adŽñ boY olamaz")
    @Size(min = 3, max = 50, message = "KullanŽñcŽñ adŽñ 3 ile 50 karakter arasŽñnda olmalŽñdŽñr")
    private String username;

    @NotBlank(message = "E-posta boY olamaz")
    @Size(max = 100, message = "E-posta en fazla 100 karakter olmalŽñdŽñr")
    @Email(message = "GeÇõerli bir e-posta adresi girin")
    private String email;

    @NotBlank(message = "?ifre boY olamaz")
    @Size(min = 6, message = "?ifre en az 6 karakter olmalŽñdŽñr")
    private String password;
}
