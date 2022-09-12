package com.booking.movieGateway.models;

import com.booking.movieGateway.exceptions.FormatException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieServiceResponse {

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Poster")
    private String posterUrl;

    public MovieServiceResponse() {
    }

    public MovieServiceResponse(String imdbId, String title, String runtime, String plot, String posterUrl) {
        this.imdbId = imdbId;
        this.title = title;
        this.runtime = runtime;
        this.plot = plot;
        this.posterUrl=posterUrl;
    }

    public Movie toMovie() throws FormatException {
        int minutes;

        try {
            final var minutesString = runtime.replace("min", "").trim();
            minutes = Integer.parseInt(minutesString);
        } catch (Exception e) {
            throw new FormatException("runtime");
        }

        return new Movie(imdbId, title, Duration.ofMinutes(minutes), plot, posterUrl);
    }
}
