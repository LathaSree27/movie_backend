package com.booking.enduser;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EndUserService {

    @Autowired
    private EndUserRepository endUserRepository;

    private EndUserModel endUserModel;

    @NotNull
    protected ResponseEntity signup(EndUser endUser) {

        if (!isValidFullName(endUser.getFullName()))
            return new ResponseEntity("Invalid name", HttpStatus.BAD_REQUEST);

        if (!isValidEmail(endUser.getEmail()))
            return new ResponseEntity("Invalid email", HttpStatus.BAD_REQUEST);

        if (!isValidPhoneNumber(endUser.getPhoneNumber()))
            return new ResponseEntity("Invalid phone number", HttpStatus.BAD_REQUEST);

        if (!isValidPassword(endUser.getPassword()))
            return new ResponseEntity("Password doesn't meet criteria required", HttpStatus.BAD_REQUEST);

        if (!endUser.getPassword().equals(endUser.getConfirmPassword()))
            return new ResponseEntity("Passwords don't match", HttpStatus.BAD_REQUEST);

        if(endUserRepository.findByEmail(endUser.getEmail()) != null)
            return new ResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);

        if(endUserRepository.findByPhoneNumber(endUser.getPhoneNumber()) != null)
            return new ResponseEntity("Phone number already exists", HttpStatus.BAD_REQUEST);

        endUserModel = new EndUserModel(endUser);

        endUserRepository.save(endUserModel);
        return new ResponseEntity("Sign up successful", HttpStatus.OK);
    }

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
}
