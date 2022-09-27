package com.booking.paymentGateway;

import com.booking.movieGateway.exceptions.FormatException;
import com.booking.paymentGateway.models.Payment;
import io.swagger.annotations.Api;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "Payment")
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private final PaymentGateway defaultPaymentGateway;

    public PaymentController(PaymentGateway defaultPaymentGateway) {
        this.defaultPaymentGateway = defaultPaymentGateway;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response acceptPayment(@RequestBody Payment payment) throws IOException, FormatException {
        System.out.println("=========hai===========");
        System.out.println("in controller");
        return defaultPaymentGateway.getPaymentStatus(payment);
    }
}
