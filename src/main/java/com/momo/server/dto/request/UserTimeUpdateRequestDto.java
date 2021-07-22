package com.momo.server.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTimeUpdateRequestDto {

    private String meetId;
    private LocalDate date;
    private String timeslot;
    private boolean possible;

}
