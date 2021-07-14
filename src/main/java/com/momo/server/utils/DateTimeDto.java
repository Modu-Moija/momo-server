package com.momo.server.utils;

import java.util.ArrayList;
import java.util.Map;

import com.momo.server.dto.response.UserMeetRespDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateTimeDto {
	private String date;
	private Map<String, Boolean> time;
	

}
