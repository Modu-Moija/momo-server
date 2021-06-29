package com.momo.server.controller;

import com.momo.server.dto.request.UserTimeSaveRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/time")
public class TimeController {


    @PutMapping
    public void saveUserTime(@RequestBody UserTimeSaveRequest request){

    }

    @GetMapping
    public void getCommonTime(){

    }


}
