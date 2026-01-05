package com.twitterclone.api.controller;

import com.twitterclone.api.dtos.responses.UserSummaryResponse;
import com.twitterclone.api.mapper.UserMapper;
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
    public List<UserSummaryResponse> getAllUsers(){
        return userService.getAllUsers().stream()
                .map(UserMapper::toSummary)
                .toList();
    }

    @GetMapping("/me")
    public UserSummaryResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByEmail(currentPrincipalName);
        return UserMapper.toSummary(user);
    }
}
