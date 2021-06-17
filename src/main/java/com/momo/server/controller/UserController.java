package com.momo.server.controller;

import com.momo.server.domain.User;
import com.momo.server.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "로그인", tags = "user")
    public ResponseEntity<?> login(User loginRequest){
        return userService.login(loginRequest);
    }
}
