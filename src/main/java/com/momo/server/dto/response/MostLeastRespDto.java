package com.momo.server.dto.response;

import com.momo.server.domain.TimeSlot;
import com.momo.server.dto.TimeSlotRespEntry;
import lombok.Setter;

import java.util.List;

@Setter
public class MostLeastRespDto {
    private List<TimeSlotRespEntry> MostTime;
    private List<TimeSlotRespEntry> LeastTime;
}
