package com.momo.server.controller;

import com.momo.server.service.MeetService;

import com.momo.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/meet")
public class MeetController {

    @Autowired
    private MeetService meetService;

    //@GetMapping()
    public String[] getMaxTime() {
        meetService.getMaxTime();
        
        return null;
    };

    //@GetMapping()
    public String[] getMinTime() {

        meetService.getMaxTime();


        return null;
    };



}
