package com.booking.enduser;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="enduser")
public class EndUserModel {
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
    @ApiModelProperty(name = "email", value = "Email of user (must be unique)", required = true, example = "test@gmail.com", position = 2)
    private String email;

    @JsonProperty
    @NotBlank(message = "Phone number must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "phoneNumber", value = "phone number of user (must be unique)", required = true, example = "9123456780", position = 3)
    private String phoneNumber;

    @JsonProperty
    @NotBlank(message = "Password name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "password", value = "Password of the user", required = true, example = "password", position = 4)
    private String password;

    public EndUserModel(EndUser endUser) {
        this.fullName = endUser.getFullName();
        this.email = endUser.getEmail();
        this.phoneNumber = endUser.getPhoneNumber();
        this.password = endUser.getPassword();
    }
}
