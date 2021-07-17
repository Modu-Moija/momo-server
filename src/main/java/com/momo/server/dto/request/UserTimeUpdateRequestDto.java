package com.momo.server.dto.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTimeUpdateRequestDto {

    private String meetId;
    private LocalDate date;
    private String timeslot;
    private boolean possible;

    public UserTimeUpdateRequestDto(String meetId, LocalDate date, String timeslot, boolean possible) {
	this.meetId = meetId;
	this.date = date;
	this.timeslot = timeslot;
	this.possible = possible;
    }

}
