package com.booking.shows.view;

import com.booking.bookings.repository.BookingRepository;
import com.booking.featureToggle.Features;
import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.shows.view.models.ShowRequest;
import com.booking.shows.view.models.ShowResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.togglz.core.Feature;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Shows")
@RestController
public class ShowController {
    ShowService showService;

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }
    @Autowired
    public BookingRepository bookingRepository;

    @GetMapping("/shows")
    @ApiOperation(value = "Fetch shows")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched shows successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public List<ShowResponse> fetchAll(@Valid @RequestParam(name = "date") Date date) throws IOException, FormatException {
        List<ShowResponse> showResponse = new ArrayList<>();
        for (Show show : showService.fetchAll(date)) {
            showResponse.add(constructShowResponse(show));
        }
        return showResponse;
    }

    @PostMapping("/schedule-movie-slot")
    @ApiOperation(value = "Add movie booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Movie scheduled successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public ResponseEntity scheduleMovie(@RequestBody ShowRequest showRequest) {
        if(Features.SECHEDULEMOVIE_FEATURE.isActive())
            return showService.addScheduledMovie(showRequest);
        return new ResponseEntity("Page Not Found!", HttpStatus.BAD_REQUEST);
    }

    private ShowResponse constructShowResponse(Show show) throws IOException, FormatException {
        Movie movie = showService.getMovieById(show.getMovieId());
        Integer bookedSeats = bookingRepository.bookedSeatsByShow(show.getId());
        return new ShowResponse(movie, bookedSeats, show.getSlot(), show);

    }
}
