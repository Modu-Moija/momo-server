package com.momo.server.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
