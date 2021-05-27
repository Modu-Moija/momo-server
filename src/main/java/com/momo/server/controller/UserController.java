package com.momo.server.controller;

import com.momo.server.domain.User;
import com.momo.server.dto.MeetInfoDto;
import com.momo.server.dto.UserInfoDto;
import com.momo.server.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.momo.server.domain.Meet;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public void login(User loginRequest, HttpServletRequest request){

        userService.login(loginRequest);
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("user"));
        System.out.println(session.getAttribute("temp"));
        session.setAttribute("user", loginRequest);
        session.setAttribute("temp", loginRequest);

    }
}
