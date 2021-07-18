package com.momo.server.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetSubInfo {

    private ArrayList<String> who;
    private String when;
    private String where;

}
