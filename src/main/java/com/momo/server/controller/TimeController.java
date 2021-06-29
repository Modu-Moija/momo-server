package com.momo.server.controller;

<<<<<<< HEAD
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.service.TimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/time")
public class TimeController {
	
	@Autowired
    private TimeService timeService;

    @PutMapping
    public ResponseEntity<String> updateUsertime(HttpServletRequest request, @RequestBody UserTimeUpdateRequestDto requestDto) {
    	

		//세션에서 유저이름찾기
		HttpSession session = request.getSession();
		User user=null;

    	timeService.updateUsertime(user, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    };
=======
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

>>>>>>> 4355ac99473f025c33d993f8d1bdde7557ca9caa

}
