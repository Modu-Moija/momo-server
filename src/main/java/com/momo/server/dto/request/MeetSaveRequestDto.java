package com.momo.server.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.momo.server.domain.Meet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetSaveRequestDto {

    @ApiModelProperty(hidden = true)
    private String meetId;
    private String title;
    private String start;
    private String end;
    
    @DateTimeFormat
    @ApiModelProperty(hidden = true)
    private LocalDateTime created;
    private int gap;

    private ArrayList<LocalDate> dates;
    @ApiModelProperty(hidden = true)
    private int num;
    @ApiModelProperty(hidden = true)
    private ArrayList<Integer> users;
    @ApiModelProperty(hidden = true)
    private int[][] times;
    private boolean center;
    private boolean video;

    @Builder
	public MeetSaveRequestDto(String meetId, String title, String start, String end, LocalDateTime created, int gap,
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
    

    public Meet toEntity() {
        return Meet.builder()
        		.meetId(meetId)
        		.title(title)
        		.start(start)
        		.end(end)
        		.created(created)
        		.gap(gap)
        		.dates(dates)
        		.num(num)
        		.users(users)
        		.times(times)
        		.center(center)
        		.video(video)
                .build();
    }
    
    
}
