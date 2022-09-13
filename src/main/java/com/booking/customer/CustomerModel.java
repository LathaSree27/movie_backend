package com.booking.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="customer")
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty
    @NotBlank(message = "Name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "name", required = true, example = "username", position = 1)
    private String fullName;

    @JsonProperty
    @NotBlank(message = "Email must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "email", value = "Email of customer (must be unique)", required = true, example = "test@gmail.com", position = 2)
    private String email;

    @JsonProperty
    @NotBlank(message = "Phone number must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "phoneNumber", value = "phone number of customer (must be unique)", required = true, example = "9123456780", position = 3)
    private String phoneNumber;

    @JsonProperty
    @NotBlank(message = "Password name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "password", value = "Password of the customer", required = true, example = "password", position = 4)
    private String password;

    @JsonProperty
    @NotBlank(message = "User name must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "username", value = "Username of customer (must be unique)", required = true, example = "user_name", position = 1)
    private String username;

    public CustomerModel(Customer customer) {
        this.fullName = customer.getFullName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.password = customer.getPassword();
        this.username = customer.getUsername();
    }

    public CustomerModel() {
    }

    public String getUsername() {
        return username;
    }
}
