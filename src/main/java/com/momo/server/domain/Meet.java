package com.momo.server.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "meet")
public class Meet {

    @MongoId
    private ObjectId id;
    private String meetId;
    private String title;
    private String start;
    private String end;
    private LocalDateTime created;
    private int gap;
    private ArrayList<LocalDate> dates;
    private int num;
    private ArrayList<BigInteger> users;
    private ArrayList<String> userNames;
    private int[][] times;
    private boolean center;
    private boolean video;

}
