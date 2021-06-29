package com.momo.server.controller;

import com.momo.server.domain.User;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request){

        User requestUser = new User();
        requestUser.setUsername(loginRequest.getUsername());
        requestUser.setMeetId(loginRequest.getMeetId());
        User user = userService.login(requestUser);
        ResponseEntity<?> response;

        if (user != null){
            request.setAttribute("authuser", user );
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            response = ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return response;

    }
}