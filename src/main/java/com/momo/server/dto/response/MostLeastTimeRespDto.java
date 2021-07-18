package com.momo.server.dto.response;

import java.util.ArrayList;

import com.momo.server.dto.CommonTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MostLeastTimeRespDto {

    private ArrayList<CommonTime> most;
    private ArrayList<CommonTime> least;

}
