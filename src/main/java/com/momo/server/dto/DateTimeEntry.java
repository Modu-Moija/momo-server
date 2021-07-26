package com.momo.server.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class DateTimeEntry {

    private LocalDate date;
    private List<TimeSlotEntry> timeslots;

}
