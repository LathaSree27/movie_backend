package com.booking.paymentGateway.models;

import com.booking.movieGateway.models.Movie;
import com.booking.utilities.serializers.duration.DurationSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import okhttp3.RequestBody;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;
@ApiModel(value = "Payment")
public class Payment  {

    @JsonProperty
    @ApiModelProperty(name = "amount", value = "Amount to be paid to book ticket", required = true, example = "100", position = 1)

    private final BigDecimal amount;

    @JsonProperty
    @ApiModelProperty(name = "creditCardNumber", value = "credit card number", dataType = "java.lang.String",required = true, example = "3566-0020-2036-0505", position = 2)
    private final String creditCardNumber;

    @JsonProperty
    @ApiModelProperty(name = "creditCardExpiration", dataType = "java.lang.String", value = "Expiry date", required = true, example = "03/2100", position = 3)
    private final String creditCardExpiration;

    @JsonProperty
    @ApiModelProperty(name = "cardSecurityCode", value = "security code of the card", required = true, example = "666", position = 4)
    private final Integer cardSecurityCode;



    public Payment(BigDecimal amount, String creditCardNumber, String creditCardExpiration, Integer cardSecurityCode) {
        this.amount = amount;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiration = creditCardExpiration;
        this.cardSecurityCode = cardSecurityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(amount, payment.amount) && Objects.equals(creditCardNumber, payment.creditCardNumber) && Objects.equals(creditCardExpiration, payment.creditCardExpiration) && Objects.equals(cardSecurityCode, payment.cardSecurityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, creditCardNumber, creditCardExpiration, cardSecurityCode);
    }


}
