package com.booking.slots;

import com.booking.shows.view.models.ShowRequest;
import com.booking.slots.repository.SlotRepository;
import com.booking.slots.repository.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SlotServiceTest {
    private SlotRepository slotRepository;
    private ShowRequest showRequest;


    @BeforeEach
    public void beforeEach() {
        slotRepository = mock(SlotRepository.class);
        showRequest = mock(ShowRequest.class);
    }

    @Test
    public void  shouldBeAbleToCallAddOrFetchSlot(){
        SlotService slotService=new SlotService(slotRepository);

        slotService.addOrFetchSlot("09:00:00");

        verify(slotRepository).findByStartTime(Time.valueOf("9:00:00"));
    }

}
