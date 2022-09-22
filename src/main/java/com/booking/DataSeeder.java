package com.booking;

import com.booking.users.User;
import com.booking.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Autowired
    PasswordEncoder passwordEncoder;

    public DataSeeder() {
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {
                repository.save(new User("seed-user-1", passwordEncoder.encode("foobar"), "admin"));
            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                repository.save(new User("seed-user-2", passwordEncoder.encode("foobar"), "admin"));
            }
        };
    }
}
