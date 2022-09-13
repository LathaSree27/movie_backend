package com.booking.customers.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @ApiModelProperty(name = "id", value = "The customer id", example = "0", position = 1)
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Invalid Name")
    @NotBlank(message = "Invalid Name")
    @ApiModelProperty(name = "customer name", value = "Name of customer", required = true, example = "Customer name", position = 2)
    private String name;

    @Column(name = "phone_number", nullable = false)
    @JsonProperty
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must have exactly 10 digits and should start with 6,7,8, or 9")
    @NotBlank(message = "Phone number must have exactly 10 digits and should start with 6,7,8, or 9")
    @ApiModelProperty(name = "phone number", value = "Phone number of the customer", required = true, example = "9933221100", position = 3)
    private String phoneNumber;

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {

    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber);
    }
}
