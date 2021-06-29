package com.momo.server.controller;

<<<<<<< HEAD
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

=======
import com.momo.server.domain.User;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
>>>>>>> 4355ac99473f025c33d993f8d1bdde7557ca9caa
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.momo.server.domain.User;
import com.momo.server.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

<<<<<<< HEAD
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, User loginRequest){
    	
    	HttpSession session = request.getSession();
    	String username = loginRequest.getUsername();
    	session.setAttribute(username, username);
    	
        return userService.login(loginRequest);
=======
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

>>>>>>> 4355ac99473f025c33d993f8d1bdde7557ca9caa
    }
}
