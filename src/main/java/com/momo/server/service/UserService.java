package com.momo.server.service;

import com.momo.server.domain.User;
import com.momo.server.exception.MeetDoesNotExistException;
import com.momo.server.repository.UserRepository;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    //로그인 메소드
    public User login(User user) {

        boolean isUserExist;
        isUserExist = userRepository.isUserExist(user);

        if (isUserExist){
            return null;
        }else{
            createUser(user);
            return user;
        }
    }

    //유저 생성
    public void createUser(User user) {
        userRepository.createUser(user);
    }

    //유저의 시간정보 저장
    public ResponseEntity<?> updateUserTimetable() {
        userRepository.updateUserTimetable();
        return ResponseEntity.ok().build();
    }

    //유저이름 찾는 메소드
    public ArrayList findUsername() {
        userRepository.findUsername();
        return null;
    }
}
