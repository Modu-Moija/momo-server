package com.momo.server.controller;

import com.google.common.hash.Hashing;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.service.MeetService;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/meet")
public class MeetController {

    @Autowired
    private MeetService meetService;

    @PostMapping
    public ResponseEntity<String> createMeet(@RequestBody MeetSaveRequestDto requestDto) {

        String hashedUrl = Hashing.sha256()
            .hashString(requestDto.toString(), StandardCharsets.UTF_8)
            .toString().substring(0,15);
        meetService.createMeet(requestDto, hashedUrl);
        return new ResponseEntity<>(hashedUrl, HttpStatus.OK);
    };

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
