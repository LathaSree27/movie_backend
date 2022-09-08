package com.booking.users;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {

    Principal principal;
    UserPrincipalService userPrincipalService;

    private static String authorizationHeader;

    @Autowired
    public UserController(UserPrincipalService userPrincipalService) {
        this.userPrincipalService = userPrincipalService;
    }

    @GetMapping("/login")
    public Map<String, Object> login(Principal principal, @RequestHeader(value="Authorization") String authorizationHeader) {
        this.principal = principal;
        this.authorizationHeader = authorizationHeader;
        String username = principal.getName();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);
        return userDetails;
    }

    @PutMapping("/login/changePassword")
    ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePassword) {
        String username = principal.getName();
        return userPrincipalService.changePasswordStatus(changePassword, username);
    }

    public static String getAuthorizationHeader() {
        return authorizationHeader;
    }
}
