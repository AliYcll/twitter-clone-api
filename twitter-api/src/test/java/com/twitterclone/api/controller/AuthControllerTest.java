package com.twitterclone.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitterclone.api.dtos.AuthResponse;
import com.twitterclone.api.dtos.RegisterRequest;
import com.twitterclone.api.exception.UserAlreadyExistsException;
import com.twitterclone.api.repository.UserRepository;
import com.twitterclone.api.security.config.SecurityConfiguration;
import com.twitterclone.api.security.jwt.JwtService;
import com.twitterclone.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfiguration.class, JwtService.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_WhenValidRequest_ShouldCreateUserAndReturnToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        AuthResponse authResponse = new AuthResponse("dummy-token", "testuser", "test@example.com", 1L);

        when(userService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"));
    }

    @Test
    void register_WhenEmailAlreadyExists_ShouldReturnConflict() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        when(userService.register(any(RegisterRequest.class))).thenThrow(new UserAlreadyExistsException("Email is already in use"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }
}


