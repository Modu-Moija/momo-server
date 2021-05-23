package com.momo.server.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MeetInfoDto {

    private MeetSummary summary;
    private ArrayList<CommonTime> most;
    private ArrayList<CommonTime> least;

}
