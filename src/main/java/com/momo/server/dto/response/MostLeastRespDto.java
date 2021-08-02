package com.momo.server.dto.response;

import com.momo.server.dto.TimeSlotRespEntry;
import lombok.Data;

import java.util.List;

@Data
public class MostLeastRespDto {
    private List<TimeSlotRespEntry> MostTime;
    private List<TimeSlotRespEntry> LeastTime;
}
