package com.twitterclone.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitterclone.api.dtos.TweetRequest;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.UserRepository;
import com.twitterclone.api.security.config.SecurityConfiguration;
import com.twitterclone.api.security.jwt.JwtService;
import com.twitterclone.api.service.TweetService;
import com.twitterclone.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TweetController.class)
@Import({SecurityConfiguration.class, JwtService.class})
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TweetService tweetService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user1@example.com")
    void createTweet_WhenAuthenticated_ShouldReturnCreatedTweet() throws Exception {
        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setContent("This is a test tweet");

        User user = new User();
        user.setUsername("user1");
        user.setEmail("user1@example.com");

        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setContent("This is a test tweet");
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        when(userService.findByEmail("user1@example.com")).thenReturn(user);
        when(tweetService.createTweet(any(TweetRequest.class), any(User.class))).thenReturn(tweet);


        mockMvc.perform(post("/api/v1/tweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tweetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a test tweet"))
                .andExpect(jsonPath("$.user.username").value("user1@example.com"));
    }

    @Test
    @WithMockUser(username = "user1@example.com")
    void deleteTweet_WhenUserIsOwner_ShouldReturnNoContent() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        when(userService.findByEmail("user1@example.com")).thenReturn(user);
        when(tweetService.deleteTweet(1L, user)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tweets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user2@example.com")
    void deleteTweet_WhenUserIsNotOwner_ShouldReturnForbidden() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(tweetService).deleteTweet(anyLong(), any(User.class));

        mockMvc.perform(delete("/api/v1/tweets/1"))
                .andExpect(status().isForbidden());
    }
}


