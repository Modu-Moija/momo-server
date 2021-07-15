package com.momo.server.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetSaveRequestDto {

    private String title;
    private String start;
    private String end;
    private ArrayList<LocalDate> dates;
    private boolean center;
    private boolean video;
    private int gap;
    
}
