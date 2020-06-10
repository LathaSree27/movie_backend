package com.booking.bookings.view;

import com.booking.bookings.BookingService;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.handlers.models.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Bookings")
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Create a booking")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a booking successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public void book(@RequestBody BookingRequest bookingRequest) throws NoSeatAvailableException {
        bookingService.book(bookingRequest.getCustomer(), bookingRequest.getShow(), bookingRequest.getDate(), bookingRequest.getNoOfSeats());
    }
}
