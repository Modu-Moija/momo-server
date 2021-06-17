package com.momo.server.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "meet")
public class Meet {

    private String meetId;
    private String title;
    private String start;
    private String end;
    private LocalDateTime created;
    private int gap;
    private ArrayList<LocalDateTime> dates;
    private int num;
    private ArrayList<Integer> users;
    private int[][] times;
    private boolean center;
    private boolean video;


}
