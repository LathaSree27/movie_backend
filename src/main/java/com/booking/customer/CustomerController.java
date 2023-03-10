package com.booking.customer;

import com.booking.featureToggle.Features;
import com.booking.handlers.models.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Customer")
@RestController
@RequestMapping("/sign-up")
public class CustomerController {
    CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Create a user")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created a user successfully"),
            @ApiResponse(code = 400, message = "Error", response = ErrorResponse.class),
    })

    ResponseEntity sign_up(@RequestBody Customer customer) {
        return customerService.signup(customer);
    }
}
