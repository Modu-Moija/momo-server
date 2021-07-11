package com.momo.server.dto.response;

import java.util.ArrayList;

import com.momo.server.utils.DateTimeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeetRespDto {


	private String meetId;
	private ArrayList<DateTimeDto> PlanList;
	private ArrayList colorDate;
	
}