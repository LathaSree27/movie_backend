package com.booking.enduser;

import com.booking.App;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EndUserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        endUserRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        endUserRepository.deleteAll();
    }

    @Test
    public void shouldSignUpSuccessfullyWhenAllCriteriaAreMet() throws Exception {

        String requestJson = objectMapper.writeValueAsString(
                new EndUser("TestUser", "demo@gmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Sign up successful"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenFullNameIsInvalid() throws Exception {
        String requestJson = objectMapper.writeValueAsString(
                new EndUser("test_user123", "demo@gmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid name"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenEmailIsInvalid() throws Exception {
        String requestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demogmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenPhoneNumberIsInvalid() throws Exception {
        String requestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demo@gmail.com", "912345678", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid phone number"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenPasswordIsInvalid() throws Exception {
        String requestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demo@gmail.com", "9123456780", "test@123", "test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password doesn't meet criteria required"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenEmailAlreadyExists() throws Exception {
        String firstRequestJson = objectMapper.writeValueAsString(
                new EndUser("TestUser", "demo@gmail.com", "9123456789", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(firstRequestJson));

        String secondRequestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demo@gmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(secondRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"))
                .andReturn();
    }

    @Test
    public void shouldNotSignUpWhenPhoneNumberAlreadyExists() throws Exception {
        String firstRequestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demo@gmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(firstRequestJson));

        String secondRequestJson = objectMapper.writeValueAsString(
                new EndUser("Test User", "demo2@gmail.com", "9123456780", "Test@123", "Test@123")
        );

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(secondRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Phone number already exists"))
                .andReturn();
    }
}
