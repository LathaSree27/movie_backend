package com.booking.shows.view.models;

import com.booking.utilities.serializers.time.TimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ShowRequest {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Date must be provided")
    private String date;

    @JsonProperty
    @JsonSerialize(using = TimeSerializer.class)
    @NotNull(message = "Slot time must be provided")
    @ApiModelProperty(name = "slot time", value = "Slot time", dataType = "java.lang.String", required = true, example = "01:30 PM")
    private String slotTime;

    @NotBlank(message = "Movie id has to be provided")
    private String movieId;

    public String getDate() {
        return date;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public String getMovieId() {
        return movieId;
    }
}
