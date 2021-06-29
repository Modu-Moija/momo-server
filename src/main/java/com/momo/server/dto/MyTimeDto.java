package com.momo.server.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MyTimeDto {

    private LocalDate date;
    private boolean isAvailable;

}
