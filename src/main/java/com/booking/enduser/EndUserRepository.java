package com.booking.enduser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndUserRepository extends JpaRepository<EndUserModel, Long> {
    public EndUserModel findByEmail(String email);
    public EndUserModel findByPhoneNumber(String phoneNumber);

}
