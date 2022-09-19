package com.booking.movieGateway;

import com.booking.config.AppConfig;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.movieGateway.models.MovieServiceResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Component
@Primary
public class DefaultMovieGateway implements MovieGateway {

    private final AppConfig appConfig;
    private final OkHttpClient httpClient;
    private final Request.Builder requestBuilder;
    private final ObjectMapper objectMapper;

    @Autowired
    public DefaultMovieGateway(AppConfig appConfig, OkHttpClient httpClient, Request.Builder requestBuilder, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Movie getMovieFromId(String id) throws IOException, FormatException {
        final var request = requestBuilder.url(appConfig.getMovieServiceHost() + "movies/" + id).build();
        final var response = httpClient.newCall(request).execute();
        final var jsonResponse = requireNonNull(response.body()).string();
        return objectMapper.readValue(jsonResponse, MovieServiceResponse.class).toMovie();
    }

    @Override
    public List<Movie> getMovie() throws IOException, FormatException {
        final var request = requestBuilder.url(appConfig.getMovieServiceHost() + "movies").build();
        final var response = httpClient.newCall(request).execute();
        final var jsonResponse = requireNonNull(response.body()).string();
        List<MovieServiceResponse> movieService = objectMapper.readValue(jsonResponse, new TypeReference<List<MovieServiceResponse>>() {
        });
        List<Movie> movies = new ArrayList<>();
        for (MovieServiceResponse movieServiceResponse : movieService) {
            movies.add(movieServiceResponse.toMovie());
        }
        return movies;
    }
}
