package com.booking.shows;

import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Constants;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.shows.view.models.ShowRequest;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieGateway movieGateway;

    @Autowired
    SlotService slotService;

    @Autowired
    public ShowService(ShowRepository showRepository, MovieGateway movieGateway) {
        this.showRepository = showRepository;
        this.movieGateway = movieGateway;
    }

    public List<Show> fetchAll(Date date) {
        return showRepository.findByDate(date);
    }

    public Movie getMovieById(String movieId) throws IOException, FormatException {
        return movieGateway.getMovieFromId(movieId);
    }

    public ResponseEntity addScheduledMovie(ShowRequest showRequest) {
        Slot slot = slotService.addOrFetchSlot(showRequest.getSlotTime());
        Show show = new Show(Date.valueOf(showRequest.getDate()), slot, new BigDecimal(randomCostGenerator()), showRequest.getMovieId());

        try {
            showRepository.save(show);
        }
        catch (Exception exception) {
            return new ResponseEntity("Specified slot is already booked for this date", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Movie scheduled successfully", HttpStatus.CREATED);
    }

    public double randomCostGenerator() {
        return Math.round((Math.random() * ((Constants.MAX_TICKET_COST - Constants.MIN_TICKET_COST) + 1)) + Constants.MIN_TICKET_COST);
    }
}
