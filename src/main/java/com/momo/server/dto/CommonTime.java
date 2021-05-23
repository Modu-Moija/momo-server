package com.momo.server.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommonTime {

    private String meetId;
    private LocalDate date;
    private String timeslot;
    private String[] available;
    private String[] unavailable;

}
