package com.momo.server.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Document(collection = "meet")
public class Meet {

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
