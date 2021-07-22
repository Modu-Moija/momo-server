package com.momo.server.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTimeUpdateRequestDto {

    @Size(max = 100, message = "meetId는 100자 이하여야 합니다.")
    @NotBlank
    private String meetId;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String timeslot;

    @NotNull
    private boolean possible;

}
