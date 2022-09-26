package com.booking.slots.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class SlotService {
    private final SlotRepository slotRepository;

    @Autowired
    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Slot addOrFetchSlot(String startTime) {
        Slot slot = slotRepository.findByStartTime(Time.valueOf(startTime));

        if(slot == null) {
            slot = new Slot(setSlotName(), Time.valueOf(startTime), Time.valueOf(addTime(startTime)));
            slotRepository.save(slot);
        }

        return slot;
    }

    private String addTime(String startTime) {
        Time time = Time.valueOf(startTime);
        LocalTime localtime = time.toLocalTime();
        localtime = localtime.plusHours(3).plusMinutes(30);
        return localtime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private String setSlotName() {
        return "slot" + (slotRepository.count() + 1);
    }
}
