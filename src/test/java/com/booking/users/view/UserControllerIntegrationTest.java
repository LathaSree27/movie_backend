package com.booking.users.view;

import com.booking.App;
import com.booking.users.*;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotChangePasswordWhenOldPasswordIsWrong() throws Exception {
        userRepository.save(new User("test-user", "password"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"pass\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotChangePasswordWhenNewPasswordDoesNotMeetCriteria() throws Exception {
        userRepository.save(new User("test-user", "password"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Password-demo@123\"," +
                "\"newPassword\": \"password-demo123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotChangePasswordWhenNewPasswordsDoNotMatch() throws Exception {
        userRepository.save(new User("test-user", "password"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "password")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"ConfirmPassword-demo@123\"," +
                "\"newPassword\": \"Password-demo@123\"," +
                "\"oldPassword\": \"password\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isBadRequest());
    }
}
