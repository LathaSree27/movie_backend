package com.booking.users;

import com.booking.featureToggle.Features;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {

    UserPrincipalService userPrincipalService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserPrincipalService userPrincipalService, PasswordEncoder passwordEncoder) {
        this.userPrincipalService = userPrincipalService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public Map<String, Object> login(Principal principal) {
            String username = principal.getName();
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("username", username);
            userDetails.put("roleName", userPrincipalService.getRoleName(username));
            userDetails.put("enable", true);
            userDetails.put("userProfileFeature", Features.USERPROFILE_FEATURE.isActive());
            if(username.equals("seed-user-1") || username.equals("seed-user-2"))
                userDetails.put("bookMovie", Features.BOOKMOVIEADMIN_FEATURE.isActive());
            else
                userDetails.put("bookMovie", Features.BOOKMOVIECUSTOMER_FEATURE.isActive());
            userDetails.put("scheduleMovieStatus", Features.SECHEDULEMOVIE_FEATURE.isActive());
            return userDetails;
    }

    @PutMapping("/login/changePassword")
    public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePassword,Principal principal) {
        String username = principal.getName();
        if(Features.USERPROFILE_FEATURE.isActive())
            return userPrincipalService.changePasswordStatus(changePassword, username);
        return new ResponseEntity("Page Not Found!", HttpStatus.BAD_REQUEST);
    }
}
