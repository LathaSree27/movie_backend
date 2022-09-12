package com.booking.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPrincipalServiceTest {

    private UserRepository mockUserRepository;
    private ChangePasswordRequest mockChangePasswordRequest;

    @BeforeEach
    void setup() {
        mockUserRepository = mock(UserRepository.class);
        mockChangePasswordRequest = mock(ChangePasswordRequest.class);
    }

    @Test
    void shouldBeAbleToChangePasswordSuccessfully() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("password");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(user.getPassword()).thenReturn("password");

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Password changed successfully", HttpStatus.OK), is(entity));
    }

    @Test
    void shouldBeAbleToCheckIncorrectOldPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("password1");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(user.getPassword()).thenReturn("password");

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckNewPasswordNotSameAsOldPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(user.getPassword()).thenReturn("Password@123");

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("New password should not be same as old password", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckNewPasswordPatternMatchesCriteria() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("password@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(user.getPassword()).thenReturn("Password@123");

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Password doesn't meet criteria required", HttpStatus.BAD_REQUEST), is(entity));
    }

    @Test
    void shouldBeAbleToCheckWhetherNewPasswordIsSameAsConfirmNewPassword() {
        User user = mock(User.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockChangePasswordRequest.getOldPassword()).thenReturn("password");
        when(mockChangePasswordRequest.getNewPassword()).thenReturn("Password@123");
        when(mockChangePasswordRequest.getConfirmNewPassword()).thenReturn("Demo@123");
        when(mockUserRepository.findByUsername("name")).thenReturn(Optional.ofNullable(user));
        when(user.getPassword()).thenReturn("password");

        ResponseEntity entity = userPrincipalService.changePasswordStatus(mockChangePasswordRequest, "name");

        assertThat(new ResponseEntity("Passwords don't match", HttpStatus.BAD_REQUEST), is(entity));
    }
}
