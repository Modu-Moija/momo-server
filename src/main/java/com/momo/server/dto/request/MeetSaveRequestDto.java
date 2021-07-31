package com.momo.server.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MeetSaveRequestDto {

    @Size(max = 30, message = "약속제목은 30자 이하여야 합니다.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;

    @NotBlank
    private String start;

    @NotBlank
    private String end;

    @NotNull(message = "날짜는 공백일 수 없습니다.")
    private ArrayList<LocalDate> dates;
    private boolean center;
    private boolean video;

    @NotNull
    @Max(value = 60, message = "gap은 60이하여야 합니다")
    private int gap;

}
