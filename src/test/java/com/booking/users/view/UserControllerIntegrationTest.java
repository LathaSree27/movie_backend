package com.booking.users.view;

import com.booking.App;
import com.booking.users.ChangePasswordRequest;
import com.booking.users.User;
import com.booking.users.UserController;
import com.booking.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
    }


    @Test
    public void shouldLoginSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowErrorMessageForInvalidCredentials() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldChangePasswordSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password"));
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", "test-user");
        mockMvc.perform(get("/login")
                        .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("Authorization", UserController.getAuthorizationHeader())
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotChangePasswordWhenOldPasswordIsWrong() throws Exception {
        userRepository.save(new User("test-user", "password"));
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", "test-user");
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"pass\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("Authorization", UserController.getAuthorizationHeader())
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotChangePasswordWhenNewPasswordDoesNotMeetCriteria() throws Exception {
        userRepository.save(new User("test-user", "password"));
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", "test-user");
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"password-demo123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("Authorization", UserController.getAuthorizationHeader())
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotChangePasswordWhenNewPasswordsDoNotMatch() throws Exception {
        userRepository.save(new User("test-user", "password"));
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", "test-user");
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"ConfirmPassword-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("Authorization", UserController.getAuthorizationHeader())
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

}
