package com.momo.server.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequestDto {

    @Size(max = 30, message = "아이디는 30자 이하여야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private String username;

    @Size(max = 100, message = "meetId는 100자 이하여야 합니다.")
    @NotBlank
    private String meetId;

    private Boolean remember;

}
