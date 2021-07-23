package com.momo.server.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "timeslot")
public class TimeSlot {

    @MongoId
    private ObjectId id;
    private String meetId;
    private ArrayList<String> users;
    private String time;
    private LocalDate date;
    private int num;

}