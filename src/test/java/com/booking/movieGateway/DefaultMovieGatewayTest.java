package com.booking.movieGateway;

import com.booking.config.AppConfig;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMovieGatewayTest {

    public static final String MOCK_SERVER_RESPONSE = "{\"imdbID\":\"id\"," +
            "\"Title\":\"title\"," +
            "\"Runtime\":\"120 min\"," +
            "\"Plot\":\"plot\"," +
            "\"Poster\":\"posterUrl\"}";
    public static final List<String> MOCK_SERVER_RESPONSE_ALL_MOVIES = List.of("{\"imdbID\":\"id\"," + "\"Title\":\"title\"," + "\"Runtime\":\"120 min\"," + "\"Plot\":\"plot\"," + "\"Poster\":\"posterUrl\"}",

            "{\"imdbID\":\"id\"," + "\"Title\":\"title\"," + "\"Runtime\":\"120 min\"," + "\"Plot\":\"plot\"," + "\"Poster\":\"posterUrl\"}");
    private MockWebServer mockWebServer;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void should_get_movie_from_service() throws IOException, FormatException {
        mockWebServer.enqueue(new MockResponse().setBody(MOCK_SERVER_RESPONSE));
        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(String.valueOf(mockWebServer.url("/")));

        final var defaultMovieGateway =
                new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(), new ObjectMapper());

        final var actualMovie = defaultMovieGateway.getMovieFromId("id");

        final var expectedMovie = new Movie("id", "title", Duration.ofMinutes(120), "plot", "posterUrl");
        assertThat(actualMovie, is(equalTo(expectedMovie)));
    }

    @Test
    public void should_get_all_movies_from_service() throws IOException, FormatException {
        mockWebServer.enqueue(new MockResponse().setBody(String.valueOf(MOCK_SERVER_RESPONSE_ALL_MOVIES)));
        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(String.valueOf(mockWebServer.url("/")));

        final var defaultMovieGateway = new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(), new ObjectMapper());

        final var actualMovieList = defaultMovieGateway.getMovie();
        System.out.println(actualMovieList);

        final var expectedMovieList = List.of(new Movie("id", "title", Duration.ofMinutes(120), "plot", "posterUrl"), new Movie("id", "title", Duration.ofMinutes(120), "plot", "posterUrl"));
        assertThat(actualMovieList, is(equalTo(expectedMovieList)));
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
