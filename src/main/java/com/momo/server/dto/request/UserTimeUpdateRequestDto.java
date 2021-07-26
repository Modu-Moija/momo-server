package com.momo.server.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.momo.server.dto.DateTimeEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTimeUpdateRequestDto {

    @Size(max = 100, message = "meetId는 100자 이하여야 합니다.")
    @NotBlank
    String meetId;

    @NotNull
    List<DateTimeEntry> usertimes;
}

//참조 https://stackoverflow.com/questions/49012470/consuming-a-nested-json-array-using-spring-boot-and-resttemplate