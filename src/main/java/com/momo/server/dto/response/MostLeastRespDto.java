package com.momo.server.dto.response;

import com.momo.server.dto.TimeSlotRespEntry;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MostLeastRespDto {
    private List<TimeSlotRespEntry> MostTime;
    private List<TimeSlotRespEntry> LeastTime;
}
