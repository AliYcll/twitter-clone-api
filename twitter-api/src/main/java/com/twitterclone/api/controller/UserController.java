package com.twitterclone.api.controller;

import com.twitterclone.api.model.User;
import com.twitterclone.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName);
    }
}
