package com.momo.server.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;

import com.momo.server.dto.MeetSubInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetSummary {

    private String title;
    private String start;
    private String end;
    private int gap;
    private ArrayList<LocalDate> dates;
    private Boolean center;
    private Boolean video;
    private MeetSubInfo meetSubInfo;

}
