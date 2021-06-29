package com.momo.server.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String username;
    private String meetId;
    private Boolean remember;
}
