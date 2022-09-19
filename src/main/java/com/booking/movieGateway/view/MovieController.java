package com.booking.movieGateway.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "ScheduleMovie")
@RestController
@RequestMapping("/movies-to-schedule")
public class MovieController {
    @Autowired
    private final MovieGateway defaultMovieGateway;

    public MovieController(MovieGateway defaultMovieGateway) {
        this.defaultMovieGateway = defaultMovieGateway;
    }

    @GetMapping
    @ApiOperation(value = "Fetch titles")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched title successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public ResponseEntity fetchAllMovies() throws IOException, FormatException {
        Map<String, String> moviesToSchedule = new HashMap<>();

        for (Movie movie : defaultMovieGateway.getMovie()) {
            moviesToSchedule.put(movie.getId(), movie.getName());
        }
        return new ResponseEntity(moviesToSchedule, HttpStatus.OK);
    }
}
