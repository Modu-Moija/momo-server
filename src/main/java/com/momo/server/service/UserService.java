package com.momo.server.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    //로그인 메소드
    public ResponseEntity<?> login(User user) {

        boolean isUserExist;
        isUserExist = userRepository.isUserExist(user);

        if (isUserExist){
            return ResponseEntity.ok().build();
        }else{
            return createUser(user);
        }
    }

    //유저 생성
    public ResponseEntity<?> createUser(User user) {
        user.set_id(userRepository.createUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


//    //유저이름 찾는 메소드
//    public ArrayList findUsername() {
//        userRepository.findUsername();
//        return null;
//    }
}
