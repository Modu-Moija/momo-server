package com.momo.server.dto.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeetRespDto {


	private String meetId;
	private LinkedHashMap<String, LinkedHashMap<String, Boolean>> PlanList;
	private ArrayList colorDate;
	
}