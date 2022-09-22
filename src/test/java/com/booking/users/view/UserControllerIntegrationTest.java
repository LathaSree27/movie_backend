package com.booking.users.view;

import com.booking.App;
import com.booking.users.ChangePasswordRequest;
import com.booking.users.User;
import com.booking.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        userRepository.save(new User("test-user", "password","admin"));
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
        userRepository.save(new User("test-user", "user@123","admin"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "user@123")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"User-demo@123\"," +
                "\"newPassword\": \"User-demo@123\"," +
                "\"oldPassword\": \"user@123\"" +
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
        userRepository.save(new User("test-user", "user@123","admin"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "user@123")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"User-demo@123\"," +
                "\"newPassword\": \"User-demo@123\"," +
                "\"oldPassword\": \"user123\"" +
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
        userRepository.save(new User("test-user", "user@123","admin"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "user@123")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"Userdemo123\"," +
                "\"newPassword\": \"Userdemo123\"," +
                "\"oldPassword\": \"user@123\"" +
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
        userRepository.save(new User("test-user", "user123","admin"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "user123")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"User-Demo@123\"," +
                "\"newPassword\": \"User-demo@123\"," +
                "\"oldPassword\": \"user123\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotChangePasswordWhenNewPasswordsAndOldPasswordIsSame() throws Exception {
        userRepository.save(new User("test-user", "User-demo@123","admin"));
        mockMvc.perform(get("/login")
                .with(httpBasic("test-user", "User-demo@123")));
        String requestJson = "{" +
                "\"confirmNewPassword\": \"User-Demo@123\"," +
                "\"newPassword\": \"User-demo@123\"," +
                "\"oldPassword\": \"User-demo@123\"" +
                "}";

        mockMvc.perform(put("/login/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(user("test-user")))
                .andExpect(status().isBadRequest());
    }
}
