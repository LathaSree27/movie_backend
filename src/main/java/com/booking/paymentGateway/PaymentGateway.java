package com.booking.paymentGateway;

import com.booking.movieGateway.exceptions.FormatException;
import com.booking.paymentGateway.models.Payment;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface PaymentGateway {
    Response getPaymentStatus(Payment payment) throws IOException, FormatException;
}
