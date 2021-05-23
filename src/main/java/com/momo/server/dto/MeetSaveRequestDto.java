package com.momo.server.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetSaveRequestDto {

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
