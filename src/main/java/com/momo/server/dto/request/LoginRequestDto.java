package com.momo.server.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequestDto {

    private String username;
    @ApiModelProperty(example = "75d57c19141e6fd")
    private String meetId;
    private Boolean remember;
}
