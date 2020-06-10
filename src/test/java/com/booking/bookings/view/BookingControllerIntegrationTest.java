package com.booking.bookings.view;

import com.booking.App;
import com.booking.bookings.repository.BookingRepository;
import com.booking.customers.repository.CustomerRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private MovieGateway movieGateway;
    private Show showOne;

    @BeforeEach
    public void beforeEach() throws IOException, FormatException {
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        slotRepository.deleteAll();
        customerRepository.deleteAll();

        when(movieGateway.getMovieFromId("movie_1"))
                .thenReturn(
                        new Movie(
                                "movie_1",
                                "Movie name",
                                Duration.ofHours(1).plusMinutes(30),
                                "Movie description"
                        )
                );
        Slot slotOne = slotRepository.save(new Slot("Test slot", Time.valueOf("09:30:00"), Time.valueOf("12:00:00")));
        showOne = showRepository.save(new Show(Date.valueOf("2020-01-01"), slotOne, new BigDecimal("249.99"), "movie_1"));
    }

    @AfterEach
    public void afterEach() {
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        slotRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void should_save_booking_and_customer_detail() throws Exception {
        final String requestJson = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"show\": " + "{\"id\":" + showOne.getId() + ",\"date\":\"2020-01-01\",\"cost\":249.99}," +
                "\"customer\": " + "{\"name\": \"Customer 1\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": 2" +
                "}";

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isCreated());

        assertThat(customerRepository.findAll().size(), is(1));
        assertThat(bookingRepository.findAll().size(), is(1));
    }

    @Test
    public void should_not_book_when_seat_is_not_available() throws Exception {
        final String moreThanAvailableSeatsRequestJson = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"show\": " + "{\"id\":" + showOne.getId() + ",\"date\":\"2020-01-01\",\"cost\":249.99}," +
                "\"customer\": " + "{\"name\": \"Customer 1\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": " + (TOTAL_NO_OF_SEATS + 1) +
                "}";


        final MvcResult mvcResult = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(moreThanAvailableSeatsRequestJson))
                .andExpect(status().is5xxServerError())
                .andReturn();

        final Exception resolvedException = mvcResult.getResolvedException();
        assertThat(resolvedException.getCause(), instanceOf(NoSeatAvailableException.class));
    }
}
