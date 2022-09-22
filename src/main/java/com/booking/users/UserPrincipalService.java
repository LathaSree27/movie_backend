package com.booking.users;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserPrincipalService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;
    @Autowired
    public UserPrincipalService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User savedUser = findUserByUsername(username);

        return new UserPrincipal(savedUser);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @NotNull
    public ResponseEntity changePasswordStatus(ChangePasswordRequest changePassword, String username) {
        Optional<User> user = userRepository.findByUsername(username);


        String password = user.get().getPassword();

        String oldPassword = changePassword.getOldPassword();

        if (!isPasswordMatch(oldPassword, password)) {
            return new ResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST);
        }

        String newPassword = changePassword.getNewPassword();

        if (isSame(newPassword, oldPassword))
            return new ResponseEntity("New password should not be same as old password", HttpStatus.BAD_REQUEST);
        if (!isValid(newPassword))
            return new ResponseEntity("Password doesn't meet criteria required", HttpStatus.BAD_REQUEST);
        if (!isSame(newPassword, changePassword.getConfirmNewPassword()))
            return new ResponseEntity("Passwords don't match", HttpStatus.BAD_REQUEST);

        user.get().setPassword(getEncode(newPassword));
        userRepository.save(user.get());
        return new ResponseEntity("Password changed successfully", HttpStatus.OK);
    }

    private String getEncode(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }

    private boolean isPasswordMatch(String oldPassword, String password) {
        return passwordEncoder.matches(oldPassword, password);
    }

    private boolean isSame(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }


    private boolean isValid(String newPassword) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,64}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newPassword);
        return matcher.matches();
    }

<<<<<<< HEAD
    public String getRoleName(String username){
        Optional<User>user=userRepository.findByUsername(username);

        if(username.equals("seed-user-2")||username.equals(("seed-user-1"))){
            user.get().setRole_name("admin");
            userRepository.save(user.get());
        }
        return userRepository.findByUsername(username).get().getRole_name();
    }
=======

>>>>>>> feb25f7 ([Deepa | Vaishnavi Bandaru] Add. password encryption for user)
}
