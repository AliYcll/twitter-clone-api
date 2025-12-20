package com.twitterclone.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitterclone.api.dtos.TweetRequest;
import com.twitterclone.api.model.Tweet;
import com.twitterclone.api.model.User;
import com.twitterclone.api.repository.TweetRepository;
import com.twitterclone.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        tweetRepository.deleteAll();

        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user2);
    }

    @Test
    @WithMockUser(username = "user1@example.com")
    void createTweet_WhenAuthenticated_ShouldReturnCreatedTweet() throws Exception {
        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setContent("This is a test tweet");

        mockMvc.perform(post("/api/tweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tweetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a test tweet"))
                .andExpect(jsonPath("$.user.username").value("user1"));
    }

    @Test
    @WithMockUser(username = "user1@example.com")
    void deleteTweet_WhenUserIsOwner_ShouldReturnNoContent() throws Exception {
        Tweet tweet = new Tweet();
        tweet.setUser(user1);
        tweet.setContent("A tweet to be deleted");
        tweet.setCreatedAt(LocalDateTime.now());
        tweet = tweetRepository.save(tweet);

        mockMvc.perform(delete("/api/tweets/" + tweet.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user2@example.com")
    void deleteTweet_WhenUserIsNotOwner_ShouldReturnForbidden() throws Exception {
        Tweet tweet = new Tweet();
        tweet.setUser(user1); 
        tweet.setContent("A tweet");
        tweet.setCreatedAt(LocalDateTime.now());
        tweet = tweetRepository.save(tweet);

        
        mockMvc.perform(delete("/api/tweets/" + tweet.getId()))
                .andExpect(status().isForbidden());
    }
}

