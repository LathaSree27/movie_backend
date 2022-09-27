package com.booking.shows;

import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.shows.view.models.ShowRequest;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ShowServiceTest {
    private ShowRepository showRepository;
    private MovieGateway movieGateway;

    private ShowRequest mockShowRequest;
    private SlotService mockSlotService;

    @BeforeEach
    public void beforeEach() {
        showRepository = mock(ShowRepository.class);
        movieGateway = mock(MovieGateway.class);
        mockShowRequest = mock(ShowRequest.class);
        mockSlotService = mock(SlotService.class);
    }

    @Test
    public void should_get_movie_by_id() throws IOException, FormatException {
        Movie movie = mock(Movie.class);
        when(movieGateway.getMovieFromId("movie_1")).thenReturn(movie);
        ShowService showService = new ShowService(showRepository, movieGateway);

        showService.getMovieById("movie_1");

        verify(movieGateway).getMovieFromId("movie_1");
    }

    @Test
    public void should_fetch_all_shows_and_set_movie_gateway() {
        List<Show> shows = new ArrayList<>();
        Slot slotOne = new Slot();
        Slot slotTwo = new Slot();
        Date date = Date.valueOf("2020-01-01");

        shows.add(new Show(
                Date.valueOf("2020-01-01"),
                slotOne,
                new BigDecimal("299.99"),
                "movie_1"));

        shows.add(new Show(
                Date.valueOf("2020-01-01"),
                slotTwo,
                new BigDecimal("299.99"),
                "movie_1"));

        when(showRepository.findByDate(date)).thenReturn(shows);
        ShowService showService = new ShowService(showRepository, movieGateway);

        List<Show> actualShows = showService.fetchAll(date);

        List<Show> expectedShows = new ArrayList<>();
        expectedShows.add(new Show(
                Date.valueOf("2020-01-01"),
                slotOne,
                new BigDecimal("299.99"),
                "movie_1"));

        expectedShows.add(new Show(
                Date.valueOf("2020-01-01"),
                slotTwo,
                new BigDecimal("299.99"),
                "movie_1"));

        assertThat(actualShows, is(equalTo(expectedShows)));
    }

}
