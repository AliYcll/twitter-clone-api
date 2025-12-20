package com.twitterclone.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 50, message = "Kullanıcı adı 3 ile 50 karakter arasında olmalıdır")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "E-posta boş olamaz")
    @Size(max = 100, message = "E-posta en fazla 100 karakter olmalıdır")
    @Email(message = "Geçerli bir e-posta adresi girin")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır") 
    @Column(nullable = false)
    private String password; 
}

