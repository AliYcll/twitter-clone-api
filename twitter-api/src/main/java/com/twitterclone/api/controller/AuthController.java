package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.AuthResponse;
import com.twitterclone.api.dtos.LoginRequest;
import com.twitterclone.api.dtos.RegisterRequest;
import com.twitterclone.api.model.User;
import com.twitterclone.api.security.jwt.JwtService;
import com.twitterclone.api.exception.UserAlreadyExistsException;
import com.twitterclone.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder; 

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        
        try {
            userService.findByEmail(request.getEmail());
            
            throw new UserAlreadyExistsException("Bu e-posta adresi zaten kullanılıyor: " + request.getEmail());
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); 

        userService.saveUser(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}

