package com.booking.paymentGateway.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)
public class PaymentResponseService {
    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private List details;

    public PaymentResponseService(String message, List details) {
        this.message = message;
        this.details = details;
    }

}
