package com.booking.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Mockito.*;

public class UserControllerTest {
    UserPrincipalService mockUserPrincipalService;
    private Principal principal;

    @BeforeEach
    void setup(){
        mockUserPrincipalService = mock(UserPrincipalService.class);
        principal = mock(Principal.class);
    }

    @Test
    void shouldBeAbleToChangeThePasswordWhenServiceIsCalled() {
        UserController userController = new UserController(mockUserPrincipalService);
        ChangePasswordRequest mockChangePasswordRequest = mock(ChangePasswordRequest.class);
        when(principal.getName()).thenReturn("name");

        userController.changePassword(mockChangePasswordRequest,principal);

        verify(mockUserPrincipalService).changePasswordStatus(mockChangePasswordRequest,"name");
    }
}
