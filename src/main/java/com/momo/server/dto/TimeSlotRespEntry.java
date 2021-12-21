package com.momo.server.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@Data
public class TimeSlotRespEntry {

    private String key;
    private ArrayList<String> availUsers;
    private ArrayList<String> unavailUsers;
    private String time;
    private LocalDate date;
    private Integer num;
}
