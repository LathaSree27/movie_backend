package com.booking.customers.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomerTest {

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void should_not_allow_customer_name_to_be_blank() {
        final Customer customer = new Customer("", "9099234568");

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Invalid Name"));
    }

    @Test
    public void should_allow_phone_number_only_10_digits() {
        final Customer customer = new Customer("Customer", "999332");

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must have exactly 10 digits and should start with 6,7,8, or 9"));
    }

    @Test
    public void should_not_allow_blank_phone_number() {
        final Customer customer = new Customer("Customer", "");

        final Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertThat(violations.iterator().next().getMessage(), is("Phone number must have exactly 10 digits and should start with 6,7,8, or 9"));
    }
}
