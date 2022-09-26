package com.booking.paymentGateway;

import com.booking.movieGateway.exceptions.FormatException;
import com.booking.paymentGateway.models.Payment;
import io.swagger.annotations.Api;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "Payment")
@RestController
public class paymentController {

    @Autowired
    DefaultPaymentGateway defaultPaymentGateway;

    public paymentController(DefaultPaymentGateway defaultPaymentGateway) {
        this.defaultPaymentGateway = defaultPaymentGateway;
    }

    @PostMapping("/payments")
//    @ApiOperation(value = "Post payment details")
//    @ResponseStatus(code = HttpStatus.OK)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Fetched title successfully"),
//            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
//    })
    public Response acceptPayment(@RequestBody Payment payment) throws IOException, FormatException {
        return defaultPaymentGateway.getPaymentStatus(payment);
    }
}
