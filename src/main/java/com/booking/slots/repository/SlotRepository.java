package com.booking.slots.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Slot findByStartTime(Time startTime);
}
