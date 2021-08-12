package com.momo.server.dto.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMeetRespDto {

    private String meetId;
    private LinkedHashMap<String, LinkedHashMap<String, Boolean>> PlanList;
    private LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> colorDate;

}