package com.booking.enduser;

import com.booking.users.ChangePasswordRequest;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "EndUser")
@RestController
public class EndUserController {

    EndUserService endUserService;

    @PostMapping("/sign-up")
    ResponseEntity sign_up(@RequestBody EndUser endUser ) {
        return endUserService.signup(endUser);
    }

}
