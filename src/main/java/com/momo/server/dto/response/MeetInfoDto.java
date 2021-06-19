package com.momo.server.dto.response;

import com.momo.server.dto.CommonTime;
import com.momo.server.dto.response.MeetSummary;
import lombok.Data;

import java.util.ArrayList;

@Data
public class MeetInfoDto {

    private MeetSummary summary;
    private ArrayList<CommonTime> most;
    private ArrayList<CommonTime> least;

}
