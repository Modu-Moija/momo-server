package com.momo.server.dto.request;

import com.momo.server.dto.MyTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTimeSaveRequest {

    private String meetId;
    private Map<LocalDate, ArrayList<MyTimeDto>> planList;

}
