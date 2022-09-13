package com.booking.customer;

import com.booking.users.User;
import com.booking.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;

    private UserRepository userRepository;
    @BeforeEach
    public void beforeEach() {
        customerRepository = mock(CustomerRepository.class);
        userRepository = mock(UserRepository.class);
        customerService = new CustomerService(customerRepository,userRepository);
    }

    @Test
    public void shouldSaveCustomerWhenCustomerSignUpSuccessfully() {
        Customer customer = new Customer("user@1", "TestUser", "demo1@gmail.com", "9123456780", "Test@123", "Test@123");
        CustomerModel customerModel = new CustomerModel(customer);
        CustomerModel mockCustomerModel = mock(CustomerModel.class);
        User user = new User("user@1","Test@123");
        User mockUser = mock(User.class);
        when(customerRepository.save(customerModel)).thenReturn(mockCustomerModel);
        when(userRepository.save(user)).thenReturn(mockUser);
        ResponseEntity expectedResponseEntity = new ResponseEntity("Sign up successful", HttpStatus.OK);

        ResponseEntity actualResponseEntity = customerService.signup(customer);

        verify(customerRepository).save(any(CustomerModel.class));
        verify(userRepository).save(any(User.class));
        assertThat(actualResponseEntity,is(equalTo(expectedResponseEntity)));
    }

}
