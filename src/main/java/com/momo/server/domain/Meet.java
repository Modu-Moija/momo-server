package com.momo.server.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Meet {

    private String meetId;
    private String title;
    private String start, end;
    private LocalDateTime created;

    private int gap;
    private ArrayList<LocalDate> dates;
    private boolean done;
    private MeetSub meetSubInfo;

    private int num = 0;
    private ArrayList<Integer> users = new ArrayList<Integer>();

    private int[] checkArray;
    private int[][] checkUser;
    private int[][] userTime;
}
