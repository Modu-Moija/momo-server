package com.momo.server.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@Data
public class TimeSlotRespEntry {

    private String meetId;
    private ArrayList<String> users;
    private String time;
    private LocalDate date;
    private Integer num;
}
