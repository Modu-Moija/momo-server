package com.momo.server.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "timeslot")
public class TimeSlot {

    private String meetId;
    private ArrayList<String> users;
    private String time;
    private LocalDate date;
    private int num;

}