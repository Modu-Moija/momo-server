package com.momo.server.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyTimeDto {

    private LocalDateTime date;
    private int[] times;

}
