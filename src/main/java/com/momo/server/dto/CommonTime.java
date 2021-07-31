package com.momo.server.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonTime {
    private String meetId;
    private LocalDate date;
    private String timeslot;
    private String[] available;
    private String[] unavailable;

}