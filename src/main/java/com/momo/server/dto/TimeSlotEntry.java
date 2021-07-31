package com.momo.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TimeSlotEntry {

    @JsonProperty("time")
    private String time;

    @JsonProperty("possible")
    private Boolean possible;

}
