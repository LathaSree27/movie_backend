package com.booking.paymentGateway;

import com.booking.config.AppConfig;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.paymentGateway.models.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

@Component
@Primary
public class DefaultPaymentGateway implements PaymentGateway {
    private final AppConfig appConfig;
    private final OkHttpClient httpClient;
    private final Request.Builder requestBuilder;
    private final ObjectMapper objectMapper;

    @Autowired
    public DefaultPaymentGateway(AppConfig appConfig, OkHttpClient httpClient, Request.Builder requestBuilder, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response getPaymentStatus(Payment payment) throws IOException, FormatException {
//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/json"), json);
//
//        Request request = new Request.Builder()
//                .url(BASE_URL + "/users/detail")
//                .post(body)
//                .build();
        RequestBody requestBody = RequestBody.create(String.valueOf(payment), MediaType.parse("application/json"));
        final var request = requestBuilder.url(appConfig.getPaymentServiceHost()).post(requestBody).build();
        final var response = httpClient.newCall(request).execute();
        System.out.println("-------------response-----------");
        System.out.println(response);
        return response;
//        final var jsonResponse = requireNonNull(response.body()).string();
//        System.out.println(jsonResponse);
//        System.out.println(jsonResponse);

//        return new ResponseEntity("Payment successful",HttpStatus.OK);
//        return new ResponseEntity(objectMapper.readValue(jsonResponse, PaymentResponseService.class),HttpStatus.OK);
    }


}
