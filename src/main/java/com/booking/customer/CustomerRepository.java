package com.booking.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
    public CustomerModel findByEmail(String email);
    public CustomerModel findByPhoneNumber(String phoneNumber);
    public CustomerModel findByUsername(String username);

}
