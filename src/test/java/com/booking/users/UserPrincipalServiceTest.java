package com.booking.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPrincipalServiceTest {

    private UserRepository mockUserRepository;
    private ChangePasswordRequest mockChangePasswordRequest;
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setup() {
        mockUserRepository = mock(UserRepository.class);
        mockChangePasswordRequest = mock(ChangePasswordRequest.class);
        mockPasswordEncoder = mock(PasswordEncoder.class);
    }

    @Test
    void shouldBeAbleEncryptPassword() {
        when(mockPasswordEncoder.encode("password")).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWO");
        String originalPassword = "password";

        String encryptedPassword = mockPasswordEncoder.encode(originalPassword);

        assertThat(encryptedPassword, is(not(originalPassword)));
    }

    @Test
    void shouldBeAbleToCheckIncorrectOldPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository, mockPasswordEncoder);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("password@1");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.encode("password")).thenReturn("$2a$12$bI9TzFPUvpMTLWMMWw6mPOSe9lDXQvQHC9GnLWRnIP09LQsPAQ0t.");
        when(user.getPassword()).thenReturn("$2a$12$bI9TzFPUvpMTLWMMWw6mPOSe9lDXQvQHC9GnLWRnIP09LQsPAQ0t.");
        when(mockPasswordEncoder.matches("password@1", "$2a$12$bI9TzFPUvpMTLWMMWw6mPOSe9lDXQvQHC9GnLWRnIP09LQsPAQ0t.")).thenReturn(false);

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckNewPasswordNotSameAsOldPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository, mockPasswordEncoder);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.encode("Password@123")).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(user.getPassword()).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(mockPasswordEncoder.matches("Password@123", "$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi")).thenReturn(true);

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("New password should not be same as old password", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckWhetherNewPasswordIsSameAsConfirmNewPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository, mockPasswordEncoder);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@12");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.encode("Password")).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(user.getPassword()).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(mockPasswordEncoder.matches("Password", "$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi")).thenReturn(true);

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Passwords don't match", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckNewPasswordPatternMatchesCriteria() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository, mockPasswordEncoder);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("password@12");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.encode("Password")).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(user.getPassword()).thenReturn("$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi");
        when(mockPasswordEncoder.matches("Password", "$2a$12$fnYc6jhKSAtDk5oWekHqBeomLMsBVVkLTV7Ol3RBXOdqox4N2vCWOi")).thenReturn(true);

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Password doesn't meet criteria required", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToChangePasswordSuccessfully() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository, mockPasswordEncoder);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(mockPasswordEncoder.encode("Password")).thenReturn("$2a$12$b3Ga/JbqIvkIho4UqOYOseF9tq0t9HqdN6boW8kz5XXh1HQuXd.4O");
        when(user.getPassword()).thenReturn("$2a$12$b3Ga/JbqIvkIho4UqOYOseF9tq0t9HqdN6boW8kz5XXh1HQuXd.4O");
        when(mockPasswordEncoder.matches("Password", "$2a$12$b3Ga/JbqIvkIho4UqOYOseF9tq0t9HqdN6boW8kz5XXh1HQuXd.4O")).thenReturn(true);

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Password changed successfully", HttpStatus.OK), is(entity));
    }

}
