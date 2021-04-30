package com.example.letsmeet.Meet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data

public class Meet {
	
	private String meetId;
	private String title;
	private String start, end;
	private LocalDateTime created;
	
	private int gap;
	private ArrayList<LocalDate> dates;
	private boolean done;
	private MeetSub meetSubInfo;
	
	private int num =0;
	private ArrayList<Integer> users = new ArrayList<Integer>();
	
	private int[] checkArray;
	private int[][] checkUser;
	private int[][] userTime;
}
