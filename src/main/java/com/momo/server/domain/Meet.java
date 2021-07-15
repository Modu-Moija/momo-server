package com.momo.server.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection = "meet")
public class Meet {

    private String meetId;
    private String title;
    private String start;
    private String end;
    private LocalDateTime created;
    private int gap;
    private ArrayList<LocalDate> dates;
    private int num;
    private ArrayList<Integer> users;
    private int[][] times;
    private boolean center;
    private boolean video;
    
    @Builder
	public Meet(String meetId, String title, String start, String end, LocalDateTime created, int gap,
			ArrayList<LocalDate> dates, int num, ArrayList<Integer> users, int[][] times, boolean center,
			boolean video) {
		this.meetId = meetId;
		this.title = title;
		this.start = start;
		this.end = end;
		this.created = created;
		this.gap = gap;
		this.dates = dates;
		this.num = num;
		this.users = users;
		this.times = times;
		this.center = center;
		this.video = video;
	}


    
    
}
