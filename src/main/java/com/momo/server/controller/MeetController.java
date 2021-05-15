package com.momo.server.controller;

import com.momo.server.service.MeetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/meet")
public class MeetController {

    @Autowired
    private MeetService meetService;


}
