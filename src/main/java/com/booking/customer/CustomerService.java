package com.booking.customer;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerModel customerModel;

    @NotNull
    protected ResponseEntity signup(Customer customer) {
        if (!isValidUsername(customer.getUsername()))
            return new ResponseEntity("Invalid username", HttpStatus.BAD_REQUEST);

        if (!isValidFullName(customer.getFullName()))
            return new ResponseEntity("Invalid name", HttpStatus.BAD_REQUEST);

        if (!isValidEmail(customer.getEmail()))
            return new ResponseEntity("Invalid email", HttpStatus.BAD_REQUEST);

        if (!isValidPhoneNumber(customer.getPhoneNumber()))
            return new ResponseEntity("Invalid phone number", HttpStatus.BAD_REQUEST);

        if (!isValidPassword(customer.getPassword()))
            return new ResponseEntity("Password doesn't meet criteria required", HttpStatus.BAD_REQUEST);

        if (!customer.getPassword().equals(customer.getConfirmPassword()))
            return new ResponseEntity("Passwords don't match", HttpStatus.BAD_REQUEST);

        if(customerRepository.findByUsername(customer.getUsername()) != null)
            return new ResponseEntity("Username already exists", HttpStatus.BAD_REQUEST);

        if(customerRepository.findByEmail(customer.getEmail()) != null)
            return new ResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);

        if(customerRepository.findByPhoneNumber(customer.getPhoneNumber()) != null)
            return new ResponseEntity("Phone number already exists", HttpStatus.BAD_REQUEST);

        //if(save(customer) != null)
        customerModel = new CustomerModel(customer);
        customerRepository.save(customerModel);
        return new ResponseEntity("Sign up successful", HttpStatus.OK);

        //return new ResponseEntity("Some error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    private CustomerModel save(Customer customer) {
//        customerModel = new CustomerModel(customer);
//        System.out.println(customerModel);
//        return customerRepository.save(customerModel);
//    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,64}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidFullName(String fullName) {
        String regex = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidUsername(String username) {
        String regex = "^[A-Za-z]+[@_]+[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
